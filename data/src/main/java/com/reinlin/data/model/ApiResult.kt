package com.reinlin.data.model

import com.google.gson.annotations.SerializedName

internal sealed class ApiResult {

    internal data class ResultExhibits(
        @SerializedName("result")
        val result: ApiExhibit
    ): ApiResult()

   internal data class ResultPlants(
        @SerializedName("result")
        val result: ApiPlants
    ): ApiResult()
}