package com.reinlin.zoo

import androidx.lifecycle.LiveData
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo

interface IZooContract {

    interface MainView {
        fun nextPage(event: InjectEvent)
        fun setToolbarTitle(title: String?)
    }

    interface PageView {
        fun notify(data: Zoo)
        fun isAnimating(): Boolean
        fun<T> onItemClicked(data: T)
    }

    interface ViewPresenter<out T> {
        val dataFromDB: LiveData<List<Data>>
        fun getDataManager(): T
        fun observe(event: ZooViewEvent)
        fun stop()
        fun isStop(): Boolean
    }
}

sealed class ZooViewEvent {
    data class FetchExhibits(val offset: Int): ZooViewEvent()
    data class FetchPlants(val keyword: String?): ZooViewEvent()
    object RefreshExhibit: ZooViewEvent()
    object RefreshPlants: ZooViewEvent()
}

sealed class InjectEvent {
    data class Detail(val data: Data.Exhibit): InjectEvent()
    data class Plant(val data: Data.Plant): InjectEvent()
}