package com.reinlin.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reinlin.domain.model.Data

@Entity(tableName = "exhibit_table")
data class DbExhibit(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "info") val info: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "picUrl") val picUrl: String?,
    @ColumnInfo(name = "URL") val URL: String?
)

internal val Data.Exhibit.toEntity: DbExhibit
    get() = DbExhibit(id, name, info, category, picUrl, URL)

internal val DbExhibit.toData: Data.Exhibit
    get() = Data.Exhibit(id, name, info, category, picUrl, URL)