package com.reinlin.data

import com.reinlin.data.model.ApiResult
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

internal interface IZooApiService {

    @GET("apiAccess?scope=$SCOPE&rid=$RID_EXHIBIT")
    suspend fun getExhibits(
        @Query("offset") offset: Int = 0
    ): Response<ApiResult.ResultExhibits>

    @GET("apiAccess?scope=$SCOPE&rid=$RID_PLANTS")
    suspend fun getPlants(
        @Query("offset") offset: Int = 0,
        @Query("q") location: String
    ): Response<ApiResult.ResultPlants>


    companion object {
        private const val BASE_URL = "https://data.taipei/opendata/datalist/"
        const val SCOPE = "resourceAquire"
        const val RID_EXHIBIT = "5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a"
        const val RID_PLANTS  = "f18de02f-b6c9-47c0-8cda-50efad621c14"

        fun instance(): IZooApiService =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IZooApiService::class.java)
    }
}