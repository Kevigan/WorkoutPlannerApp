package com.example.workoutplannerapp

import org.junit.Test
import org.junit.Assert.*
import com.example.workoutplannerapp.data.*

class WorkoutUnitTest {
    @Test
    fun totalReps_isCorrect() {
        val workout = Workout(
            title = "Test",
            exercises = listOf(
                WorkoutItem.Exercise("Pushups", 10),
                WorkoutItem.Exercise("Pullups", 5),
                WorkoutItem.Pause(2)
            )
        )
        val totalR = workout.totalReps()
        val total = workout.exercises
            .filterIsInstance<WorkoutItem.Exercise>()
            .sumOf { it.reps }

        assertEquals(15, totalR)
    }
}