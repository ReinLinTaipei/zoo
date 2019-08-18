package com.reinlin.zoo.detail

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo
import com.reinlin.domain.repository.ILocalRepository
import com.reinlin.domain.repository.IRemoteRepository
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.ZooViewEvent
import com.reinlin.zoo.common.BasePresenter
import com.reinlin.zoo.common.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailListPresenter(
    dispatcher: DispatcherProvider,
    private val remoteService: IRemoteRepository,
    private val localService: ILocalRepository,
    private val data: LiveData<List<Data.Plant>>,
    private val dataManager: DetailListManager,
    private val view: IZooContract.DetailView
) :
    BasePresenter(dispatcher),
    CoroutineScope,
    IZooContract.ViewPresenter<DetailListManager> {

    override val dataFromDB: LiveData<List<Data>>
        get() = Transformations.map(data) {
            it.map { db -> db as Data }
        }

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

    private fun fetchPlants(keyword: String?) = launch {
        if (TextUtils.isEmpty(keyword).not()) {
            remoteService.getPlants(0, keyword!!).let { result ->
                when (result) {
                    is Zoo.Plants -> {
                        result.plants.map {
                            localService.insert(it)
                        }
                    }
                    else -> view.onFetchDone(result)
                }
            }
        } else
            view.onFetchDone(Zoo.NoData)
    }
}