package com.reinlin.data.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reinlin.data.model.db.DbConverter
import com.reinlin.data.model.db.DbExhibit
import com.reinlin.data.model.db.DbPlant

@Database(entities = [DbExhibit::class, DbPlant::class], version = 3, exportSchema = false)
@TypeConverters(DbConverter::class)
abstract class ZooDatabase : RoomDatabase() {

    abstract fun exhibitDao(): ExhibitDao

    abstract fun plantDao(): PlantDao

    companion object {

        @Volatile
        private var INSTANCE: ZooDatabase? = null

        fun getDatabase(context: Context): ZooDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ZooDatabase::class.java,
                    "Zoo_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
    }
}