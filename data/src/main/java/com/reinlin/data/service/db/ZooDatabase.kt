package com.reinlin.data.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reinlin.data.model.Table

@Database(entities = [Table.Exhibit::class, Table.Plant::class], version = 1)
internal abstract class ZooDatabase : RoomDatabase() {

    internal abstract fun exhibitDao(): ExhibitDao

    internal abstract fun plantDao(): PlantDao

    companion object {

        @Volatile
        private var INSTANCE: ZooDatabase? = null

        internal fun getDatabase(context: Context): ZooDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ZooDatabase::class.java,
                    "Zoo_database"
                ).build()
                INSTANCE = instance
                instance
            }
    }
}