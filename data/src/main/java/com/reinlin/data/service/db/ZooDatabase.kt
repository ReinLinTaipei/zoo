package com.reinlin.data.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reinlin.data.model.local.DbZoo

@Database(entities = [DbZoo.Exhibit::class, DbZoo.Plant::class], version = 2, exportSchema = false)
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