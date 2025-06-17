package com.example.workoutplannerapp

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workoutplannerapp.viewmodels.WorkoutViewModel
import com.example.workoutplannerapp.views.CreateWorkoutView
import com.example.workoutplannerapp.views.MainView
import com.example.workoutplannerapp.views.WorkoutView

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    workoutViewModel: WorkoutViewModel
) {
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) {
            MainView(
                navController,
                workoutViewModel
            )
        }

        composable(Screen.WorkoutScreen.route) {
            val selectedWorkout by workoutViewModel.selectedWorkout.observeAsState()

            selectedWorkout?.let { workout ->
                WorkoutView(
                    workout = workout,
                    onBack = {
                        workoutViewModel.clearSelectedWorkout()
                        navController.popBackStack()
                    }
                )
            } ?: run {
                // Optional: fallback if workout is null (e.g., on process death)
                Text("No workout selected.")
            }
        }

        composable(Screen.CreateWorkoutScreen.route){
            CreateWorkoutView(workoutViewModel)
        }
    }
}
