package com.reinlin.data.model.api

import com.google.gson.annotations.SerializedName
import com.reinlin.domain.model.Data


internal data class ApiExhibit(
    @SerializedName("offset")
    val offset: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("results")
    val details: List<DetailExhibit>
)

internal data class DetailExhibit(
    @SerializedName("_id")
    val id: Int,

    @SerializedName("E_Name")
    val name: String,

    @SerializedName("E_Info")
    val info: String,

    @SerializedName("E_Category")
    val category: String,

    @SerializedName("E_URL")
    val URL: String,

    @SerializedName("E_Pic_URL")
    val picURL: String
)

internal val DetailExhibit.toExhibit: Data.Exhibit
    get() = Data.Exhibit(
        id,
        name,
        info,
        category,
        picURL,
        URL
    )
