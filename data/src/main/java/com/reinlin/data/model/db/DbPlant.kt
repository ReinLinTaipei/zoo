package com.reinlin.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reinlin.domain.model.Data

@Entity(tableName = "plant_table")
data class DbPlant(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name")       val name: String? = null,
    @ColumnInfo(name = "nameEn")     val nameEn: String? = null,
    @ColumnInfo(name = "nameLatin")  val nameLatin: String? = null,
    @ColumnInfo(name = "alsoKnown")  val alsoKnown: String? = null,
    @ColumnInfo(name = "locations")  val locations: String? = null,
    @ColumnInfo(name = "familyType") val familyType: String? = null,
    @ColumnInfo(name = "genusType")  val genusType: String? = null,
    @ColumnInfo(name = "briefInfo")  val briefInfo: String? = null,
    @ColumnInfo(name = "detail")     val detail: String? = null,
    @ColumnInfo(name = "function")   val function: String? = null,
    @ColumnInfo(name = "picUrl")     val picUrl: String? = null
)

internal val Data.Plant.toEntity: DbPlant
    get() = DbPlant(id,
        name,
        nameEn,
        nameLatin,
        alsoKnown,
        locations,
        familyType,
        genusType,
        briefInfo,
        detail,
        function,
        picUrl)

internal val DbPlant.toData: Data.Plant
    get() = Data.Plant(id,
        name,
        nameEn,
        nameLatin,
        alsoKnown,
        locations,
        familyType,
        genusType,
        briefInfo,
        detail,
        function,
        picUrl)