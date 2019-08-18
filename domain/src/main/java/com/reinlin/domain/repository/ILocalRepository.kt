package com.reinlin.domain.repository

import com.reinlin.domain.model.Data

interface ILocalRepository {

//    suspend fun getExhibits(): List<Data.Exhibit>
//
//    suspend fun getPlants(): List<Data.Plant>

    suspend fun getExhibits(startId: Int, count: Int = 10): List<Data.Exhibit>

    suspend fun getPlants(startId: Int, count: Int = 10): List<Data.Plant>

    suspend fun insert(data: Data)

    suspend fun deleteExhibits()

    suspend fun deletePlants()
}