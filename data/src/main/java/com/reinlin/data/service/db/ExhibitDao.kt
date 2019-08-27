package com.reinlin.data.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reinlin.data.model.local.DbZoo

@Dao
interface ExhibitDao: BaseDao<DbZoo.Exhibit> {

    @Query("SELECT * FROM table_exhibit ORDER BY id ASC")
    fun getData(): LiveData<List<DbZoo.Exhibit>>

    @Query("SELECT * FROM table_exhibit WHERE id = :id")
    fun getExhibit(id: Int) : DbZoo.Exhibit?

    @Query("SELECT * FROM table_exhibit WHERE id >= :startId ORDER BY id ASC LIMIT :limit")
    suspend fun getExhibits(startId: Int, limit: Int = 10): List<DbZoo.Exhibit>

    @Query("DELETE FROM table_exhibit")
    suspend fun deleteAll()
}