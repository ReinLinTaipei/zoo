package com.reinlin.zoo.brief

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo
import com.reinlin.domain.repository.ILocalRepository
import com.reinlin.domain.repository.IRemoteRepository
import com.reinlin.zoo.IZooContract
import com.reinlin.zoo.ZooViewEvent
import com.reinlin.zoo.common.BasePresenter
import com.reinlin.zoo.common.DispatcherProvider
import com.reinlin.zoo.common.TAG
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BriefListPresenter(dispatcher: DispatcherProvider,
                         private val remoteService: IRemoteRepository,
                         private val localService: ILocalRepository,
                         data: LiveData<List<Data.Exhibit>>,
                         private val dataManager: BriefListManager,
                         private val view: IZooContract.BriefView
):
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
        when(event) {
            is ZooViewEvent.FetchExhibits -> fetchExhibits(event.offset)
        }
    }

    private fun fetchExhibits(offset: Int) = launch {
            Log.i(TAG, "fetch start, offset: $offset")

            remoteService.getExhibits(offset, 10).let {
                when (it) {
                    is Zoo.Exhibits -> {
                        it.exhibits.map { exhibit ->
                            localService.insert(exhibit)
                        }
                    }
                    else -> view.onFetchDone(it)
                }
            }
        }
}
