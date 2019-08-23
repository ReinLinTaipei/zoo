package com.reinlin.domain.usecase

import com.reinlin.domain.model.Data
import com.reinlin.domain.repository.ILocalRepository
import com.reinlin.domain.repository.IRemoteRepository

const val CACHE_LIMIT = 10

class GetBriefUseCase(
    val localRepository: ILocalRepository,
    val remoteRepository: IRemoteRepository
) {
    private val cachedList = arrayListOf<Data.Exhibit>()
    private val pendingList = arrayListOf<Process.Pending>()
    private var localProcess: Process = Process.Stop
    private var lastId = 0

    fun useCached(
        position: Int,
        notify: (Data.Exhibit) -> Unit,
        queryLocal: (pending: Process.Pending) -> Unit
    ) {
        cachedList.apply {
                singleOrNull { it.position == position }
                    ?.let(notify)
                    ?: addPending(Process.Pending(position, ++lastId), queryLocal)
        }
    }

    private fun addPending(pending: Process.Pending,
                           queryLocal: (pending: Process.Pending) -> Unit) {
        pendingList.add(pending)
        if (localProcess == Process.Stop) {
            localProcess = Process.Querying
            consumePending(queryLocal)
        }
    }

    private fun consumePending(queryLocal: (pending: Process.Pending) -> Unit) {
        if (localProcess == Process.Querying) {
            if (pendingList.isNotEmpty())
                queryLocal(pendingList.removeAt(0))
            else
                localProcess = Process.Stop
        }
    }

    suspend fun fetchLocal(
        pending: Process.Pending,
        notify: (Data.Exhibit) -> Unit,
        queryLocal: (pending: Process.Pending) -> Unit
    ) {
//        localRepository.getExhibit(pending.id)?.let { data ->
//            pendingList.remove(pending)
//            addCached(data.copy(position = pending.position))
//            notify(data)
//        }

        consumePending(queryLocal)
    }

    private fun addCached(data: Data.Exhibit) {
        cachedList.add(data)
        if (cachedList.size > CACHE_LIMIT) cachedList.removeAt(0)
    }

    sealed class Process {
        object Stop : Process()
        object Querying : Process()
        data class Pending(val position: Int, val id: Int) : Process()
    }
}