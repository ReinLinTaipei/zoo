package com.reinlin.data.repository

import androidx.annotation.WorkerThread
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.reinlin.data.service.db.ExhibitDao
import com.reinlin.data.service.db.PlantDao
import com.reinlin.data.model.local.toData
import com.reinlin.data.model.local.toEntity
import com.reinlin.domain.model.Data
import com.reinlin.domain.repository.ILocalRepository


class LocalRepositoryImpl(
    private val exhibitDao: ExhibitDao,
    private val plantDao: PlantDao
) : ILocalRepository {


    val exhibitsFromDB: LiveData<List<Data.Exhibit>>
        get() = Transformations.map(exhibitDao.getData()) {
            it.map { db -> db.toData }
        }

    val plantsFromDB: LiveData<List<Data.Plant>>
        get() = Transformations.map(plantDao.getData()) {
            it.map { db -> db.toData }
        }

    override suspend fun getExhibits(startId: Int, count: Int): List<Data.Exhibit> =
        exhibitDao.getExhibits(startId, count).map { it.toData }

    override suspend fun getPlants(startId: Int, count: Int): List<Data.Plant> =
        plantDao.getPlants(startId, count).map { it.toData }

    @WorkerThread
    override suspend fun insert(data: Data) {
        when(data) {
            is Data.Exhibit -> exhibitDao.insert(data.toEntity)
            is Data.Plant   -> plantDao.insert(data.toEntity)
        }
    }

    override suspend fun deleteExhibits() =
        exhibitDao.deleteAll()

    override suspend fun deletePlants() =
        plantDao.deleteAll()
}

