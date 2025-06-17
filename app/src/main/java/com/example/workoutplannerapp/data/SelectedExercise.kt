package com.example.workoutplannerapp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class SelectedExercise(
    val definition: ExerciseDefinition,
    var sets: MutableState<Int> = mutableStateOf(3),
    var reps: MutableState<Int> = mutableStateOf(10),
    val durationSeconds: MutableState<Int> = mutableStateOf(20)
)


