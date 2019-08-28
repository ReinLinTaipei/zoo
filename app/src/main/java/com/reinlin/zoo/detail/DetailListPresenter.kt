package com.reinlin.zoo.detail

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo
import com.reinlin.domain.usecase.GetDataUseCase
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.ZooViewEvent
import com.reinlin.zoo.common.BasePresenter
import com.reinlin.zoo.common.DispatcherProvider
import com.reinlin.zoo.common.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailListPresenter(
    dispatcher: DispatcherProvider,
    private val useCase: GetDataUseCase,
    private val data: LiveData<List<Data.Plant>>,
    private val dataManager: DetailListManager,
    private val view: IZooContract.PageView
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

    override fun stop() = job.cancel()

    override fun isStop(): Boolean = job.isCancelled

    override fun getDataManager(): DetailListManager = dataManager

    override fun observe(event: ZooViewEvent) {
        when (event) {
            is ZooViewEvent.FetchPlants  -> fetchPlants(event.keyword)
            is ZooViewEvent.RefreshPlants -> launch { useCase.deletePlants() }
        }
    }

    private fun fetchPlants(keyword: String?) = launch {
        Log.i(TAG, "fetch plants: $keyword")
        if (TextUtils.isEmpty(keyword).not()) {
            useCase.fetchPlants(0, keyword!!, view::notify)
        } else
            view.notify(Zoo.NoData)
    }
}