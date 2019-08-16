package com.reinlin.data.model.remote

import com.google.gson.annotations.SerializedName

internal sealed class ApiResult {

    internal data class Exhibits(
        @SerializedName("result")
        val result: ApiExhibit
    ): ApiResult()

   internal data class Plants(
        @SerializedName("result")
        val result: ApiPlants
    ): ApiResult()
}