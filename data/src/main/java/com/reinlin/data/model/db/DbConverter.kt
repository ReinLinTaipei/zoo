package com.reinlin.data.model.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DbConverter {

    @TypeConverter
    inline fun<reified T> fromList(data: List<T>?): String? =
        data?.let { Gson().toJson(data) }

    @TypeConverter
    inline fun<reified T> toList(json: String?): List<T>? =
        json?.let { Gson().fromJson(json, object : TypeToken<List<T>>(){}.type) }

    @TypeConverter
    fun fromExhibit(data: DbExhibit): String =
        Gson().toJson(data)

    @TypeConverter
    fun fromPlant(data: DbPlant): String =
        Gson().toJson(data)

    @TypeConverter
    fun toExhibit(json: String): DbExhibit =
        Gson().fromJson(json, DbExhibit::class.java)

    @TypeConverter
    fun toPlant(json: String): DbPlant =
        Gson().fromJson(json, DbPlant::class.java)
}

