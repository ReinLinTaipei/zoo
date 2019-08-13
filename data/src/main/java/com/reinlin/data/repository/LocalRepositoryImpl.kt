package com.reinlin.data.repository

import androidx.annotation.WorkerThread
import com.reinlin.data.model.toEntity
import com.reinlin.data.service.db.ExhibitDao
import com.reinlin.data.service.db.PlantDao
import com.reinlin.domain.model.Data
import com.reinlin.domain.repository.ILocalRepository


class LocalRepositoryImpl(
    private val exhibitDao: ExhibitDao,
    private val plantDao: PlantDao
) : ILocalRepository {

    override suspend fun getExhibits(startId: Int, count: Int): List<Data.Exhibit> =
        exhibitDao.getExhibits(startId, count).map { it.exhibit }

    override suspend fun getPlants(startId: Int, count: Int): List<Data.Plant> =
        plantDao.getPlants(startId, count).map { it.plant }

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

