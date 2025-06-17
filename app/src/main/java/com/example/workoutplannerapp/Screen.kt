package com.example.workoutplannerapp

sealed class Screen(val route: String, val title: String) {
    object MainScreen : Screen("main_screen", "Main")
    object WorkoutScreen: Screen("workout_screen/{workoutTitle}", "Workout") {
        fun createRoute(workoutTitle: String) = "workout_screen/$workoutTitle"
    }
    object CreateWorkoutScreen: Screen("create_workout_screen","CreateWorkout")

}