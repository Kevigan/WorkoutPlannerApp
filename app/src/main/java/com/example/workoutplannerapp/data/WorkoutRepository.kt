package com.example.workoutplannerapp.repository

import com.example.workoutplannerapp.data.*

class WorkoutRepository(private val dao: WorkoutDao) {

    val allWorkouts = dao.getAllWorkoutsWithItems()

    suspend fun insertWorkoutWithItems(title: String, mode: WorkoutMode, items: List<WorkoutItemEntity>) {
        val workoutId = dao.insertWorkout(WorkoutEntity(title = title, mode = mode)).toInt()
        val itemsWithWorkoutId = items.map { it.copy(workoutId = workoutId) }
        dao.insertWorkoutItems(itemsWithWorkoutId)
    }

    suspend fun updateWorkout(workout: WorkoutEntity, newItems: List<WorkoutItemEntity>) {
        dao.updateWorkout(workout)

        dao.deleteWorkoutItemsByWorkoutId(workout.id) // delete old items

        val updatedItems = newItems.map { it.copy(workoutId = workout.id) }
        dao.insertWorkoutItems(updatedItems) // insert new/updated items
    }
}
