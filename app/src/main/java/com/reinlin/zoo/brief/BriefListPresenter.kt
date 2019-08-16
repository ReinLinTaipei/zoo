package com.reinlin.zoo.brief

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
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

    override val dataFromDB: LiveData<List<Data>>
        get() = liveData {
            Log.i(TAG, "live data watch local db")
            emit(localService.getExhibits().map { it as Data })
        }

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
                        Log.i(TAG, "fetch done: ${it.exhibits.size}")
                        it.exhibits.map { exhibit ->
                            Log.i(TAG, "add data in db ${exhibit.name}")
                            localService.insert(exhibit)
                        }
                    }
                    else -> view.onFetchDone(it)
                }
            }
        }
}
