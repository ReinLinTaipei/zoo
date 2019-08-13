package com.reinlin.data.service.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reinlin.data.model.Table

@Dao
interface ExhibitDao {

    @Query("SELECT * FROM exhibit_table WHERE id >= :startId ORDER BY id ASC LIMIT :limit")
    fun getExhibits(startId: Int, limit: Int = 10): List<Table.Exhibit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exhibit: Table.Exhibit)

    @Query("DELETE FROM exhibit_table")
    fun deleteAll()
}