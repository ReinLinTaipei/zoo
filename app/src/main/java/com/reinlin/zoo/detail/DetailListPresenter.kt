package com.reinlin.zoo.detail

import android.text.TextUtils
import com.reinlin.domain.model.Zoo
import com.reinlin.domain.repository.IRemoteRepository
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.ZooViewEvent
import com.reinlin.zoo.common.BasePresenter
import com.reinlin.zoo.common.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailListPresenter(dispatcher: DispatcherProvider,
                          private val service: IRemoteRepository,
                          private val dataManager: DetailListManager,
                          private val view: IZooContract.DetailView
):
    BasePresenter(dispatcher),
    CoroutineScope,
    IZooContract.DetailPresenter {

    init {
        job = Job()
    }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.contextUI + job

    override fun clear() = job.cancel()

    override fun getDataManager(): DetailListManager = dataManager

    override fun observe(event: ZooViewEvent) {
        when (event) {
            is ZooViewEvent.FetchPlants -> fetchPlants(event.keyword)
        }
    }

    private fun fetchPlants(keyword: String?) {
        job = launch {
            if (TextUtils.isEmpty(keyword).not()) {
                val result = service.getPlants(0, keyword!!)
                view.onFetchDone(result)
            }
            else
                view.onFetchDone(Zoo.NoData)
        }
    }
}