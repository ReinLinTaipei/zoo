package com.reinlin.zoo

import androidx.fragment.app.Fragment
import com.reinlin.domain.model.Data
import com.reinlin.domain.model.Zoo
import com.reinlin.zoo.brief.BriefListManager
import com.reinlin.zoo.detail.DetailListManager

interface IZooContract {

    interface MainView {
        fun nextPage(event: InjectEvent)
        fun setToolbarTitle(title: String?)
    }

    interface BriefView {
        fun onFetchDone(result: Zoo)
    }

    interface DetailView {
        fun onFetchDone(result: Zoo)
    }

    interface IAdapter<in T> {
        fun isAnimating(): Boolean
        fun onItemClicked(data: T)
    }

    interface BriefPresenter {
        fun getDataManager(): BriefListManager
        fun observe(event: ZooViewEvent)
        fun clear()
    }

    interface DetailPresenter {
        fun getDataManager(): DetailListManager
        fun observe(event: ZooViewEvent)
        fun clear()
    }
}

sealed class ZooViewEvent {
    object FetchExhibits: ZooViewEvent()
    data class FetchPlants(val keyword: String?): ZooViewEvent()
}

sealed class InjectEvent {
    data class Detail(val data: Data.Exhibit): InjectEvent()
    data class Plant(val data: Data.Plant): InjectEvent()
}