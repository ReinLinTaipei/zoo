package com.reinlin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.reinlin.data.service.db.ExhibitDao
import com.reinlin.data.model.local.toData
import com.reinlin.data.model.local.toEntity
import com.reinlin.domain.model.Data
import com.reinlin.domain.repository.ILocalRepository


class LocalExhibitRepoImpl(
    private val dao: ExhibitDao
) : ILocalRepository<Data.Exhibit> {

    val exhibitsFromDB: LiveData<List<Data.Exhibit>>
        get() = Transformations.map(dao.getData()) {
            it.map { db -> db.toData }
        }

    override suspend fun getData(id: Int): Data.Exhibit? =
        dao.getExhibit(id)?.toData

    override suspend fun getData(startId: Int, count: Int): List<Data.Exhibit> =
        dao.getExhibits(startId, count).map { it.toData }

    override suspend fun insert(data: Data.Exhibit) =
        dao.insert(data.toEntity)

    override suspend fun insertAll(data: List<Data.Exhibit>) =
        dao.insertAll(data.map { it.toEntity })

    override suspend fun deleteAll() =
        dao.deleteAll()
}

