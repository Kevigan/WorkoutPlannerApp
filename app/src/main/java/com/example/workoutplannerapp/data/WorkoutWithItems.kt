package com.example.workoutplannerapp.data

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithItems(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val items: List<WorkoutItemEntity>
)

