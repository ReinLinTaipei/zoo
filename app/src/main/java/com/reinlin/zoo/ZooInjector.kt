package com.reinlin.zoo

import com.reinlin.data.ZooRepositoryImpl
import com.reinlin.domain.repository.IRemoteRepository
import com.reinlin.zoo.brief.BriefListFragment
import com.reinlin.zoo.brief.BriefListManager
import com.reinlin.zoo.brief.BriefListPresenter
import com.reinlin.zoo.common.DispatcherProvider
import com.reinlin.zoo.detail.DetailListFragment
import com.reinlin.zoo.detail.DetailListManager
import com.reinlin.zoo.detail.DetailListPresenter
import com.reinlin.zoo.plant.PlantDetailFragment
import com.reinlin.zoo.plant.PlantDetailManager

class ZooInjector {

    private val remoteRepository: IRemoteRepository by lazy {
        ZooRepositoryImpl()
    }

    private val exhibitListManager: BriefListManager by lazy {
        BriefListManager()
    }

    private val detailListManager: DetailListManager by lazy {
        DetailListManager()
    }

    private val plantDetailManager: PlantDetailManager by lazy {
        PlantDetailManager()
    }

    fun buildBriefPresenter(briefView: BriefListFragment) {
        BriefListPresenter(
            DispatcherProvider,
            service = remoteRepository,
            dataManager = exhibitListManager,
            view = briefView
        ).let {
            briefView.presenter = it
        }
    }

    fun buildDetailPresenter(detailView: DetailListFragment) {
        DetailListPresenter(
            DispatcherProvider,
            service = remoteRepository,
            dataManager = detailListManager,
            view = detailView
        ).let {
            detailView.presenter = it
        }
    }

    fun buildPlantManager(plantDetailView: PlantDetailFragment) {
        plantDetailView.dataManager = plantDetailManager
    }
}