package com.reinlin.domain.usecase

import com.reinlin.domain.model.*
import com.reinlin.domain.repository.ILocalRepository
import com.reinlin.domain.repository.IRemoteRepository

const val PAGE_COUNT = 10

class GetDataUseCase(
    private val localExhibitRepo: ILocalRepository<Data.Exhibit>,
    private val localPlantRepo: ILocalRepository<Data.Plant>,
    private val remoteRepository: IRemoteRepository
) {

    suspend fun fetchExhibits(offset: Int, count: Int, notify: (result: Zoo) -> Unit) {
        remoteRepository.getExhibits(offset, count).let {
            when(it) {
                is Zoo.Exhibits -> localExhibitRepo.insertAll(it.exhibits)
                else -> notify(it)
            }
        }
    }

    suspend fun fetchPlants(offset: Int, keyword: String, notify: (result: Zoo) -> Unit) {
        remoteRepository.getPlants(offset, keyword).let {
            when(it) {
                is Zoo.Plants -> localPlantRepo.insertAll(it.plants)
                else -> notify(it)
            }
        }
    }

    suspend fun deleteExhibits() =
        localExhibitRepo.deleteAll()
}