package com.reinlin.zoo.brief

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Notify
import com.reinlin.domain.model.Pending
import com.reinlin.domain.usecase.GetBriefUseCase
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.ZooViewEvent
import com.reinlin.zoo.common.BasePresenter
import com.reinlin.zoo.common.DispatcherProvider
import com.reinlin.zoo.common.TAG
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BriefListPresenter(
    dispatcher: DispatcherProvider,
    private val userCase: GetBriefUseCase,
    data: LiveData<List<Data.Exhibit>>,
    private val dataManager: BriefListManager,
    private val view: IZooContract.BriefView
) :
    BasePresenter(dispatcher),
    CoroutineScope,
    IZooContract.ViewPresenter<BriefListManager> {

    init {
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
            is ZooViewEvent.FetchExhibits -> fetchData(event.offset)
        }
    }

    private fun fetchData(position: Int) = launch {
        Log.i(TAG, "fetch start: $position")
        userCase.getCached(position).let {
            when(it) {
                is Pending.Cached -> view.notify(Notify.Forward(it.data))
                else -> userCase.queryLocal(it, view::notify)
            }
        }
    }
}
