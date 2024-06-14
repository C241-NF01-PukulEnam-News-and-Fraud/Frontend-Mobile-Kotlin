    package com.example.pukul6.data.database

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.room.Dao
    import androidx.room.Delete
    import androidx.room.Insert
    import androidx.room.OnConflictStrategy
    import androidx.room.Query

    @Dao
    interface ClassificationDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(classificationResult: ClassificationResult)

        @Query("SELECT * FROM classification_results ORDER BY timestamp DESC")
        fun getAllClassificationResults(): LiveData<List<ClassificationResult>>

        @Query("SELECT * FROM classification_results ORDER BY timestamp DESC LIMIT 1")
        fun getLatestClassificationResult(): LiveData<ClassificationResult>

        @Delete
        suspend fun delete(classificationResult: ClassificationResult)

        @Query("DELETE FROM classification_results")
        suspend fun deleteAll()
    }
