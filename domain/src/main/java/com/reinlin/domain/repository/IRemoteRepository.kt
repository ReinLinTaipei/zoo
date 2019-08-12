package com.reinlin.domain.repository

import com.reinlin.domain.model.Zoo

interface IRemoteRepository {

    suspend fun getExhibits(offset: Int): Zoo

    suspend fun getPlants(offset: Int, location: String): Zoo
}