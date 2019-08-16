package com.reinlin.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reinlin.domain.model.Data

sealed class DbZoo {

    @Entity(tableName = "table_exhibit")
    data class Exhibit(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Int,

        @Embedded(prefix = "exhibit")
        val exhibit: Data.Exhibit
    ): DbZoo()


    @Entity(tableName = "table_plant")
    data class Plant(

        @PrimaryKey
        @ColumnInfo(name = "id")
        val id: Int,

        @Embedded(prefix = "plant")
        val plant: Data.Plant
    ): DbZoo()
}