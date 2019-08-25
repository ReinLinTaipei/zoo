package com.reinlin.zoo

import android.app.Application
import androidx.lifecycle.LiveData
import com.reinlin.data.repository.LocalRepositoryImpl
import com.reinlin.data.repository.RemoteRepositoryImpl
import com.reinlin.data.service.db.ZooDatabase
import com.reinlin.domain.model.Data
import com.reinlin.domain.repository.ILocalRepository
import com.reinlin.domain.repository.IRemoteRepository
import com.reinlin.domain.usecase.GetBriefUseCase
import com.reinlin.zoo.brief.BriefListFragment
import com.reinlin.zoo.brief.BriefListManager
import com.reinlin.zoo.brief.BriefListPresenter
import com.reinlin.zoo.common.DispatcherProvider
import com.reinlin.zoo.detail.DetailListFragment
import com.reinlin.zoo.detail.DetailListManager
import com.reinlin.zoo.detail.DetailListPresenter
import com.reinlin.zoo.plant.PlantDetailFragment
import com.reinlin.zoo.plant.PlantDetailManager

class ZooInjector(application: Application) {

    private val remoteRepository: IRemoteRepository by lazy {
        RemoteRepositoryImpl()
    }

    private val database: ZooDatabase by lazy {
        ZooDatabase.getDatabase(application.applicationContext)
    }

    private val localRepository: ILocalRepository by lazy {
        LocalRepositoryImpl(database.exhibitDao(), database.plantDao())
    }

    private val briefUsecase: GetBriefUseCase by lazy {
        GetBriefUseCase(localRepository, remoteRepository)
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
            userCase = briefUsecase,
            data = (localRepository as LocalRepositoryImpl).exhibitsFromDB,
            dataManager = exhibitListManager,
            view = briefView
        ).let {
            briefView.presenter = it
            briefView
        }

    fun buildDetailPresenter(detailView: DetailListFragment): DetailListFragment =
        DetailListPresenter(
            DispatcherProvider,
            remoteService = remoteRepository,
            localService = localRepository,
            data = (localRepository as LocalRepositoryImpl).plantsFromDB,
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