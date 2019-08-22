package com.reinlin.domain.usecase

import com.reinlin.domain.model.Data
import com.reinlin.domain.repository.ILocalRepository
import com.reinlin.domain.repository.IRemoteRepository

class GetBriefUseCase(val localRepository: ILocalRepository,
                      val remoteRepository: IRemoteRepository) {

    private val cachedList = arrayListOf<Data.Exhibit>()
    private val pendingList = arrayListOf<Int>()
    private var localProcess: Process = Process.Stop

    fun fetchFromCached(position: Int, notify: (Data.Exhibit) -> Unit, queryLocal: (id: Int, position: Int) -> Unit) {
        cachedList.apply {
            singleOrNull { it.position == position }
                ?.let(notify)
                ?:let {
                    val nextId = if(isEmpty()) 0 else last().id + 1
                    addPendingForQuery(nextId) {
                        id ->  queryLocal(id, position)
                    }
                }
        }
    }

    private fun addPendingForQuery(nextId: Int, queryLocal: (id: Int) -> Unit) {
        pendingList.add(nextId)
        if (localProcess is Process.Stop) {
            localProcess = Process.Querying
            queryLocal(pendingList.removeAt(0))
        }
    }

    suspend fun fetchLocal(id: Int, position: Int, notify: (Data.Exhibit) -> Unit) {
        val data = localRepository.getExhibit(id)
        cachedList.removeAt(0)
        cachedList.add(data.copy(position = position))
        pendingList.remove(id)
        notify(data)
        if (pendingList.isEmpty())
            localProcess = Process.Stop
    }

    sealed class Process {
        object Stop: Process()
        object Querying: Process()
    }
}