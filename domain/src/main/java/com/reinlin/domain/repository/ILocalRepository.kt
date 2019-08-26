package com.reinlin.domain.repository



interface ILocalRepository<T> {

    suspend fun getData(id: Int): T?

    suspend fun getData(startId: Int, count: Int): List<T>

    suspend fun insert(data: T)

    suspend fun insertAll(data: List<T>)

    suspend fun deleteAll()

}