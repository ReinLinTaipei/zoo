package com.reinlin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.reinlin.data.model.local.toData
import com.reinlin.data.model.local.toEntity
import com.reinlin.data.service.db.PlantDao
import com.reinlin.domain.model.Data
import com.reinlin.domain.repository.ILocalRepository

class LocalPlantRepoImp(val dao: PlantDao) : ILocalRepository<Data.Plant> {

    val plantsFromDB: LiveData<List<Data.Plant>> = Transformations.map(dao.getData()) {
        it.map { plant -> plant.toData }
    }

    override suspend fun getData(id: Int): Data.Plant? =
        dao.getPlant(id)?.toData

    override suspend fun getData(startId: Int, count: Int): List<Data.Plant> =
        dao.getPlants(startId, count).map { it.toData }

    override suspend fun insert(data: Data.Plant) =
        dao.insert(data.toEntity)

    override suspend fun insertAll(data: List<Data.Plant>) =
        dao.insertAll(data.map { it.toEntity })

    override suspend fun deleteAll() =
        dao.deleteAll()
}