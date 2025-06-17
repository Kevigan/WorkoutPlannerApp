package com.example.workoutplannerapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    // Insert Workout
    @Insert
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Query("DELETE FROM workout_items WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutItemsByWorkoutId(workoutId: Int)

    // Insert Items (after workout insertion)
    @Insert
    suspend fun insertWorkoutItems(items: List<WorkoutItemEntity>)

    // Load full workouts with items
    @Transaction
    @Query("SELECT * FROM workouts")
    fun getAllWorkoutsWithItems(): Flow<List<WorkoutWithItems>>
}
