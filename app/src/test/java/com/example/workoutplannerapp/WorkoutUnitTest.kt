package com.example.workoutplannerapp

import org.junit.Test
import org.junit.Assert.*
import com.example.workoutplannerapp.data.*

class WorkoutLogicTest {

    val items = listOf(
        WorkoutItemEntity(workoutId = 0, type = WorkoutItemType.EXERCISE, name = "Pushups", sets = 3, reps = 10),
        WorkoutItemEntity(workoutId = 0, type = WorkoutItemType.PAUSE, durationSeconds = 30),
        WorkoutItemEntity(workoutId = 0, type = WorkoutItemType.EXERCISE, name = "Squats", sets = 2, reps = 15),
        WorkoutItemEntity(workoutId = 0, type = WorkoutItemType.PAUSE, durationSeconds = 60)
    )

    @Test
    fun workoutItems_areInCorrectOrder() {
        assertEquals(WorkoutItemType.EXERCISE, items[0].type)
        assertEquals("Pushups", items[0].name)

        assertEquals(WorkoutItemType.PAUSE, items[1].type)
        assertEquals(30, items[1].durationSeconds)

        assertEquals(WorkoutItemType.EXERCISE, items[2].type)
        assertEquals("Squats", items[2].name)

        assertEquals(WorkoutItemType.PAUSE, items[3].type)
        assertEquals(60, items[3].durationSeconds)
    }

    @Test
    fun totalReps_isCorrect_withAllFields() {
        val items = listOf(
            WorkoutItemEntity(
                workoutId = 1,
                type = WorkoutItemType.EXERCISE,
                name = "Pushups",
                sets = 3,
                reps = 10,
                durationSeconds = 30
            ),
            WorkoutItemEntity(
                workoutId = 1,
                type = WorkoutItemType.EXERCISE,
                name = "Pullups",
                sets = 2,
                reps = 5,
                durationSeconds = 20
            ),
            WorkoutItemEntity(
                workoutId = 1,
                type = WorkoutItemType.PAUSE,
                name = "Rest",
                sets = null,
                reps = null,
                durationSeconds = 60
            )
        )

        val totalReps = items
            .filter { it.type == WorkoutItemType.EXERCISE }
            .sumOf { (it.sets ?: 1) * (it.reps ?: 0) }

        assertEquals(3 * 10 + 2 * 5, totalReps)
    }

    @Test
    fun totalDuration_isCorrect_withAllFields() {
        val items = listOf(
            WorkoutItemEntity(
                workoutId = 1,
                type = WorkoutItemType.EXERCISE,
                name = "Pushups",
                sets = 3,
                reps = 10,
                durationSeconds = 30
            ),
            WorkoutItemEntity(
                workoutId = 1,
                type = WorkoutItemType.EXERCISE,
                name = "Pullups",
                sets = 2,
                reps = 5,
                durationSeconds = 20
            ),
            WorkoutItemEntity(
                workoutId = 1,
                type = WorkoutItemType.PAUSE,
                name = "Rest",
                sets = null,
                reps = null,
                durationSeconds = 60
            )
        )

        val totalDuration = items.sumOf { it.durationSeconds ?: 0 }

        assertEquals(30 + 20 + 60, totalDuration)
    }

    @Test
    fun filterOnlyExercises_returnsCorrectItems() {
        // Given
        val items = listOf(
            WorkoutItemEntity(workoutId = 0, type = WorkoutItemType.EXERCISE, name = "Pushups"),
            WorkoutItemEntity(workoutId = 0, type = WorkoutItemType.PAUSE, durationSeconds = 30),
            WorkoutItemEntity(workoutId = 0, type = WorkoutItemType.EXERCISE, name = "Squats"),
        )

        // When
        val onlyExercises = items.filter { it.type == WorkoutItemType.EXERCISE }

        // Then
        assertEquals(2, onlyExercises.size)
        assertTrue(onlyExercises.all { it.type == WorkoutItemType.EXERCISE })
        assertEquals("Pushups", onlyExercises[0].name)
        assertEquals("Squats", onlyExercises[1].name)
    }
}
