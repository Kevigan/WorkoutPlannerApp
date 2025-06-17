package com.example.workoutplannerapp.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromWorkoutMode(value: WorkoutMode): String = value.name

    @TypeConverter
    fun toWorkoutMode(value: String): WorkoutMode = WorkoutMode.valueOf(value)
}