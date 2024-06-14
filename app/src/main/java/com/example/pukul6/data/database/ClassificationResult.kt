package com.example.pukul6.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "classification_results")
@TypeConverters(Converters::class)
data class ClassificationResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val inputText: String,
    val probability: Float,
    val entities: List<String>,
    val bias: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)
