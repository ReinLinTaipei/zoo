package com.reinlin.data.service.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T)
}