package com.reinlin.zoo

import com.reinlin.data.repository.RemoteRepositoryImpl
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
        RemoteRepositoryImpl()
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

    fun buildBriefPresenter(briefView: BriefListFragment): BriefListFragment =
        BriefListPresenter(
            DispatcherProvider,
            service = remoteRepository,
            dataManager = exhibitListManager,
            view = briefView
        ).let {
            briefView.presenter = it
            briefView
        }

    fun buildDetailPresenter(detailView: DetailListFragment): DetailListFragment =
        DetailListPresenter(
            DispatcherProvider,
            service = remoteRepository,
            dataManager = detailListManager,
            view = detailView
        ).let {
            detailView.presenter = it
            detailView
        }

    fun buildPlantManager(plantDetailView: PlantDetailFragment): PlantDetailFragment =
        plantDetailView.apply {
            dataManager = plantDetailManager
        }
}