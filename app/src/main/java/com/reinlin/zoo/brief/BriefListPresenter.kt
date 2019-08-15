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
    IZooContract.BriefPresenter {

    init {
        job = Job()
    }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.contextUI + job

    override fun clear() = job.cancel()

    override fun getExhibits(): LiveData<List<Data.Exhibit>> = liveData {
        emit(localService.getExhibits())
    }

    override fun getDataManager(): BriefListManager = dataManager

    override fun observe(event: ZooViewEvent) {
        when(event) {
            is ZooViewEvent.UpdateExhibits -> fetchExhibits(event.offset)
        }
    }

    private fun fetchExhibits(offset: Int) = launch {
            Log.i(TAG, "fetch offset: $offset")
            remoteService.getExhibits(offset, 10).let {
                Log.i(TAG, "fetch done $it")
                when (it) {
                    is Zoo.Exhibits -> {
                        it.exhibits.map { exhibit ->
                            Log.i(TAG, "fetch done ${exhibit.name}")
                            localService.insert(exhibit)
                        }
                    }
                    else -> view.onFetchDone(it)
                }
            }
        }
}
