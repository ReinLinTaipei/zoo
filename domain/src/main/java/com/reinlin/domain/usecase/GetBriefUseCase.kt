package com.reinlin.domain.usecase

import com.reinlin.domain.model.*
import com.reinlin.domain.repository.ILocalRepository
import com.reinlin.domain.repository.IRemoteRepository

const val CACHE_LIMIT = 30
const val PAGE_COUNT = 10

class GetBriefUseCase(
    private val localRepository: ILocalRepository,
    private val remoteRepository: IRemoteRepository
) {
    private val cachedList = arrayListOf<Data.Exhibit>()

    fun getCached(
        position: Int
    ): Pending {
        return cachedList.run {
            singleOrNull { it.position == position }
                ?.let {
                    val id = cachedList.indexOf(it)
                    val count = cachedList.size - id
                    Pending.Cached(cachedList.subList(id, if (count > PAGE_COUNT) PAGE_COUNT else count))
                }
                ?: let {
                    when {
                        it.isEmpty()                -> Pending.Forward(0, 0)
                        position < first().position -> Pending.Back(position, first().id - 1)
                        else                        -> Pending.Forward(position, last().id + 1)
                    }
                }
        }
    }

    suspend fun queryLocal(
        pending: Pending,
        notify: (data: Notify) -> Unit
    ) {
        when (pending) {
            is Pending.Back -> queryBack(1, pending, notify)
            is Pending.Forward -> queryForward(pending, notify)
        }
    }

    private suspend fun queryBack(
        count: Int,
        pending: Pending.Back,
        notify: (data: Notify) -> Unit
    ) {
        if (count > PAGE_COUNT || pending.position < 0 || pending.offset < 0) {
            val backData = cachedList.subList(0, count - 1)
            notify(Notify.Back(backData))
            return
        }

        val singleLocal = localRepository.getExhibit(pending.offset)
        singleLocal?.let {
            val data = it.copy(position = pending.position)
            addCached(Cached.Back(data))
        }

        queryBack(
            count + 1,
            Pending.Back(
                pending.position - 1,
                pending.offset - 1
            ),
            notify
        )
    }

    private suspend fun queryForward(
        pending: Pending.Forward,
        notify: (data: Notify) -> Unit
    ) {
        localRepository.getExhibits(pending.offset, PAGE_COUNT).let { data ->
            if (data.isNotEmpty()) {
                var count = pending.position
                data.map { addCached(Cached.Forward(it.copy(position = count++))) }
                notify(Notify.Forward(data))
            }
            else
                fetchRemote(pending, notify)
        }
    }

    private suspend fun fetchRemote(pending: Pending.Forward, notify: (Notify) -> Unit) {
        remoteRepository.getExhibits(pending.offset, PAGE_COUNT).let {
            when(it) {
                is Zoo.Exhibits -> {
                    var count = pending.position
                    it.exhibits.map { data ->
                        addCached(Cached.Forward(data.copy(position = count++)))
                        localRepository.insert(data)
                    }
                    notify(Notify.Forward(it.exhibits))
                }
                else -> Notify.Result(it)
            }
        }
    }

    private fun addCached(data: Cached) {
        when(data) {
            is Cached.Back    -> cachedList.add(0, data.data as Data.Exhibit)
            is Cached.Forward -> cachedList.add(data.data as Data.Exhibit)
        }
        removeData(data)
    }

    private fun removeData(data: Cached) {
        if (cachedList.size > CACHE_LIMIT)
            when(data) {
                is Cached.Back -> cachedList.last { cachedList.remove(it) }
                is Cached.Forward -> cachedList.removeAt(0)
            }
    }

}