package com.example.workoutplannerapp.repository

import com.example.workoutplannerapp.data.*

class WorkoutRepository(private val dao: WorkoutDao) {

    val allWorkouts = dao.getAllWorkoutsWithItems()

    suspend fun insertWorkoutWithItems(title: String, items: List<WorkoutItemEntity>) {
        val workoutId = dao.insertWorkout(WorkoutEntity(title = title)).toInt()
        val itemsWithWorkoutId = items.map { it.copy(workoutId = workoutId) }
        dao.insertWorkoutItems(itemsWithWorkoutId)
    }
}
