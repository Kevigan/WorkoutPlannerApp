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
import com.example.workoutplannerapp.views.PlayView
import com.example.workoutplannerapp.views.TimedPlayView
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
                    navController = navController,
                    workoutViewModel = workoutViewModel,
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
            CreateWorkoutView(navController ,workoutViewModel)
        }

        composable(Screen.EditWorkoutScreen.route) {
            val selectedWorkout by workoutViewModel.selectedWorkout.observeAsState()
            selectedWorkout?.let { workout ->
                CreateWorkoutView(
                    navController = navController,
                    workoutViewModel = workoutViewModel,
                    workoutToEdit = workout,
                    isEditing = true
                )
            } ?: run {
                Text("No workout loaded.")
            }
        }

        composable(Screen.PlayScreen.route) {
            val selectedWorkout by workoutViewModel.selectedWorkout.observeAsState()
            selectedWorkout?.let { workout ->
                PlayView(workout = workout) {
                    navController.popBackStack()
                }
            }
        }

        composable(Screen.TimedPlayScreen.route) {
            val selectedWorkout by workoutViewModel.selectedWorkout.observeAsState()
            selectedWorkout?.let { workout ->
                TimedPlayView(workout = workout) {
                    navController.popBackStack()
                }
            }
        }

    }
}
