package com.reinlin.zoo

import com.reinlin.domain.model.Zoo
import com.reinlin.zoo.brief.BriefListManager
import com.reinlin.zoo.detail.DetailListManager

interface IZooContract {

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
    object NotThing: ZooViewEvent()
    object FetchExhibits: ZooViewEvent()
    data class FetchPlants(val keyword: String?): ZooViewEvent()
}