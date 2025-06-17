package com.example.workoutplannerapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val mode: WorkoutMode = WorkoutMode.MANUAL
)

@Entity(
    tableName = "workout_items",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workoutId: Int,
    val type: WorkoutItemType,
    val name: String?,           // For Exercise
    val sets: Int?,              // NEW: For Exercise
    val reps: Int?,              // For Exercise
    val durationSeconds: Int?    // For Pause
)


enum class WorkoutItemType {
    EXERCISE, PAUSE
}
