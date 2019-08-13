package com.reinlin.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reinlin.domain.model.Data

sealed class Table {

    @Entity(tableName = "exhibit_table")
    data class Exhibit(
        @PrimaryKey @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "exhibit") val exhibit: Data.Exhibit
    ) : Table()

    @Entity(tableName = "plant_table")
    data class Plant(
        @PrimaryKey @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "plant") val plant: Data.Plant
    ) : Table()
}

internal val Data.Exhibit.toEntity: Table.Exhibit
        get() = Table.Exhibit(id, this)

internal val Data.Plant.toEntity: Table.Plant
        get() = Table.Plant(id, this)