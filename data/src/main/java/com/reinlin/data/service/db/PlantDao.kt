package com.reinlin.data.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reinlin.data.model.local.DbZoo

@Dao
interface PlantDao {

    @Query("SELECT * FROM table_plant ORDER BY id ASC")
    fun getData(): LiveData<List<DbZoo.Plant>>

    @Query("SELECT * FROM table_plant WHERE id >= :startId ORDER BY id ASC LIMIT :limit")
    fun getPlants(startId: Int, limit: Int): List<DbZoo.Plant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: DbZoo.Plant)

    @Query("DELETE FROM table_plant")
    fun deleteAll()
}