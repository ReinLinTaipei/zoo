package com.reinlin.zoo.brief

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.reinlin.domain.model.Data
import com.reinlin.domain.usecase.GetDataUseCase
import com.reinlin.domain.usecase.PAGE_COUNT
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.ZooViewEvent
import com.reinlin.zoo.common.BasePresenter
import com.reinlin.zoo.common.DispatcherProvider
import com.reinlin.zoo.common.TAG
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BriefListPresenter(
    dispatcher: DispatcherProvider,
    private val useCase: GetDataUseCase,
    data: LiveData<List<Data.Exhibit>>,
    private val dataManager: BriefListManager,
    private val view: IZooContract.PageView
) :
    BasePresenter(dispatcher),
    CoroutineScope,
    IZooContract.ViewPresenter<BriefListManager> {

    init {
        Log.i(TAG, "brief presenter init")
        job = Job()
    }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.contextUI + job

    override fun clear() = job.cancel()

    override val dataFromDB: LiveData<List<Data>> =
        Transformations.map(data) { it.map { db -> db as Data } }

    override fun getDataManager(): BriefListManager = dataManager

    override fun observe(event: ZooViewEvent) {
        when (event) {
            is ZooViewEvent.FetchExhibits -> launch { useCase.fetchExhibits(event.offset, PAGE_COUNT, view::notify) }
            is ZooViewEvent.RefreshExhibit -> launch { useCase.deleteExhibits() }
        }
    }
}
