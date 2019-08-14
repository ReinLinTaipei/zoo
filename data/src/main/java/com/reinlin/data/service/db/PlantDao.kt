package com.reinlin.data.service.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reinlin.data.model.db.DbPlant

@Dao
interface PlantDao {

    @Query("SELECT * FROM plant_table ORDER BY id ASC")
    fun getData(): List<DbPlant>

    @Query("SELECT * FROM plant_table WHERE id >= :startId ORDER BY id ASC LIMIT :limit")
    fun getPlants(startId: Int, limit: Int): List<DbPlant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: DbPlant)

    @Query("DELETE FROM plant_table")
    fun deleteAll()
}