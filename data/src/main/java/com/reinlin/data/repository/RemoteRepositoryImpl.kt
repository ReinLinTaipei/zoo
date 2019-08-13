package com.reinlin.data.repository

import com.reinlin.data.service.IZooApiService
import com.reinlin.data.model.api.toExhibit
import com.reinlin.data.model.api.toPlant
import com.reinlin.domain.model.Zoo
import com.reinlin.domain.repository.IRemoteRepository

class RemoteRepositoryImpl : IRemoteRepository {

    private val service = IZooApiService.instance()

    override suspend fun getExhibits(offset: Int): Zoo =
        try {
            service.getExhibits(offset).run {
                if (this.isSuccessful) {
                    this.body()?.let {
                        Zoo.Exhibits(
                            it.result.offset,
                            it.result.details.map { detail -> detail.toExhibit }
                        )
                    } ?: Zoo.NoData
                }
                else
                    Zoo.NoData
            }
        } catch (e: Exception) {
            Zoo.Exception(e.toString())
        }


    override suspend fun getPlants(offset: Int, location: String): Zoo =
        try {
            service.getPlants(offset, location).run {
                if (this.isSuccessful) {
                    this.body()?.let {
                        Zoo.Plants(
                            it.result.offset,
                            it.result.details.map { detail -> detail.toPlant }
                        )
                    } ?: Zoo.NoData
                }
                else
                    Zoo.NoData

            }
        } catch (e: Exception) {
            Zoo.Exception(e.toString())
        }
}



