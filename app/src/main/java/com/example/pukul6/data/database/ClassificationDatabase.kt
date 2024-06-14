package com.example.pukul6.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ClassificationResult::class], version = 2,exportSchema = false)
@TypeConverters(Converters::class)
abstract class ClassificationDatabase : RoomDatabase() {
    abstract fun classificationDao(): ClassificationDao

    companion object {
        @Volatile
        private var instance: ClassificationDatabase? = null

        fun getInstance(context: Context): ClassificationDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ClassificationDatabase::class.java,
                    "classification_database"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}
