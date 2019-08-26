package com.reinlin.zoo

import androidx.lifecycle.LiveData
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo

interface IZooContract {

    interface MainView {
        fun nextPage(event: InjectEvent)
        fun setToolbarTitle(title: String?)
    }

    interface BriefView {
        fun notify(result: Zoo)
    }

    interface DetailView {
        fun onFetchDone(result: Zoo)
    }

    interface IAdapter<in T> {
        fun isAnimating(): Boolean
        fun onItemClicked(data: T)
    }

    interface ViewPresenter<out T> {
        val dataFromDB: LiveData<List<Data>>
        fun getDataManager(): T
        fun observe(event: ZooViewEvent)
        fun clear()
    }
}

sealed class ZooViewEvent {
    data class FetchExhibits(val offset: Int): ZooViewEvent()
    data class FetchPlants(val keyword: String?): ZooViewEvent()
}

sealed class InjectEvent {
    data class Detail(val data: Data.Exhibit): InjectEvent()
    data class Plant(val data: Data.Plant): InjectEvent()
}