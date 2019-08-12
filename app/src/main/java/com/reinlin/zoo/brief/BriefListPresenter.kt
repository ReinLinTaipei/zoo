package com.reinlin.zoo.brief

import com.reinlin.domain.repository.IRemoteRepository
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.ZooViewEvent
import com.reinlin.zoo.common.BasePresenter
import com.reinlin.zoo.common.DispatcherProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BriefListPresenter(dispatcher: DispatcherProvider,
                         private val service: IRemoteRepository,
                         private val dataManager: BriefListManager,
                         private val view: IZooContract.BriefView
):
    BasePresenter(dispatcher),
    CoroutineScope,
    IZooContract.BriefPresenter {

    init {
        job = Job()
    }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.contextUI + job

    override fun clear() = job.cancel()

    override fun getDataManager(): BriefListManager = dataManager

    override fun observe(event: ZooViewEvent) {
        when(event) {
            is ZooViewEvent.FetchExhibits -> fetchExhibits()
        }
    }

    private fun fetchExhibits() = launch {
            val exhibits = service.getExhibits(0)
            view.onFetchDone(exhibits)
        }
}
