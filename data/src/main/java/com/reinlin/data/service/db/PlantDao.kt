package com.reinlin.data.service.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.reinlin.data.model.local.DbZoo

@Dao
interface PlantDao: BaseDao<DbZoo.Plant> {


    @Query("SELECT * FROM table_plant ORDER BY id ASC")
    fun getData(): LiveData<List<DbZoo.Plant>>

    @Query("SELECT * FROM table_plant WHERE id = :id")
    fun getPlant(id: Int): DbZoo.Plant?

    @Query("SELECT * FROM table_plant WHERE id >= :startId ORDER BY id ASC LIMIT :limit")
    fun getPlants(startId: Int, limit: Int): List<DbZoo.Plant>


    @Query("DELETE FROM table_plant")
    suspend fun deleteAll()

    @Transaction
    suspend fun updateData(data: List<DbZoo.Plant>) {
        deleteAll()
        insertAll(data)
    }
}