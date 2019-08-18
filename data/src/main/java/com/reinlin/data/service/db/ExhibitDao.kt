package com.reinlin.data.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reinlin.data.model.local.DbZoo

@Dao
interface ExhibitDao {

    @Query("SELECT * FROM table_exhibit ORDER BY id DESC")
    fun getData(): LiveData<List<DbZoo.Exhibit>>

    @Query("SELECT * FROM table_exhibit WHERE id >= :startId ORDER BY id ASC LIMIT :limit")
    suspend fun getExhibits(startId: Int, limit: Int = 10): List<DbZoo.Exhibit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exhibit: DbZoo.Exhibit)

    @Query("DELETE FROM table_exhibit")
    fun deleteAll()
}