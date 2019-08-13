package com.reinlin.data.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reinlin.data.model.Table

@Dao
interface PlantDao {

    @Query("SELECT * FROM plant_table WHERE id >= :startId ORDER BY id ASC LIMIT :limit")
    fun getPlants(startId: Int, limit: Int): List<Table.Plant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: Table.Plant)

    @Query("DELETE FROM plant_table")
    fun deleteAll()
}