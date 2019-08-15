package com.reinlin.data.service.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reinlin.data.model.db.DbExhibit

@Dao
interface ExhibitDao {

    @Query("SELECT * FROM exhibit_table ORDER BY id DESC")
    suspend fun getData(): List<DbExhibit>

    @Query("SELECT * FROM exhibit_table WHERE id >= :startId ORDER BY id ASC LIMIT :limit")
    suspend fun getExhibits(startId: Int, limit: Int = 10): List<DbExhibit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exhibit: DbExhibit)

    @Query("DELETE FROM exhibit_table")
    fun deleteAll()
}