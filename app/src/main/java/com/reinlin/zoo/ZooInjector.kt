package com.reinlin.zoo

import android.app.Application
import com.reinlin.data.repository.LocalExhibitRepoImpl
import com.reinlin.data.repository.LocalPlantRepoImp
import com.reinlin.data.repository.RemoteRepositoryImpl
import com.reinlin.data.service.db.ZooDatabase
import com.reinlin.domain.model.Data
import com.reinlin.domain.repository.ILocalRepository
import com.reinlin.domain.repository.IRemoteRepository
import com.reinlin.domain.usecase.GetDataUseCase
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

    private val localExhibitRepo: ILocalRepository<Data.Exhibit> by lazy {
        LocalExhibitRepoImpl(database.exhibitDao())
    }

    private val localPlantRepo: ILocalRepository<Data.Plant> by lazy {
        LocalPlantRepoImp(database.plantDao())
    }

    private val getDataUseCase: GetDataUseCase by lazy {
        GetDataUseCase(localExhibitRepo, localPlantRepo, remoteRepository)
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
            useCase = getDataUseCase,
            data = (localExhibitRepo as LocalExhibitRepoImpl).exhibitsFromDB,
            dataManager = exhibitListManager,
            view = briefView
        ).let {
            briefView.presenter = it
            briefView
        }

    fun buildDetailPresenter(detailView: DetailListFragment): DetailListFragment =
        DetailListPresenter(
            DispatcherProvider,
            useCase = getDataUseCase,
            data = (localPlantRepo as LocalPlantRepoImp).plantsFromDB,
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