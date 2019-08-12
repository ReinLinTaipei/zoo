package com.reinlin.data.model

import com.google.gson.annotations.SerializedName
import com.reinlin.domain.model.Data

internal data class ApiPlants(
    @SerializedName("offset")
    val offset: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("results")
    val details: List<DetailPlant>
)

internal data class DetailPlant(
    @SerializedName("_id")
    val id: Int,

    @SerializedName("F_Name_Ch")
    val name: String,

    @SerializedName("F_Name_En")
    val nameEn: String?,

    @SerializedName("F_Name_Latin")
    val nameLatin: String?,

    @SerializedName("F_AlsoKnown")
    val alsoKnown: String?,

    @SerializedName("F_Location")
    val locations: String?,

    @SerializedName("F_Family")
    val familyCategory: String?,

    @SerializedName("F_Genus")
    val genusCategory: String?,

    @SerializedName("F_Brief")
    val briefInfo: String?,

    @SerializedName("F_Feature")
    val featureInfo: String?,

    @SerializedName("F_Function&Application")
    val functionInfo: String?,

    @SerializedName("F_Pic01_URL")
    val picURL: String?
)

internal val DetailPlant.toPlant: Data.Plant
    get() = Data.Plant(
        id,
        name,
        nameEn,
        nameLatin,
        alsoKnown,
        locations,
        familyCategory,
        genusCategory,
        briefInfo,
        featureInfo,
        functionInfo,
        picURL
    )
