package com.example.workoutplannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplannerapp.ui.theme.WorkoutPlannerAppTheme
import com.example.workoutplannerapp.viewmodels.WorkoutViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var workoutViewModel: WorkoutViewModel

        workoutViewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)

        setContent {
            WorkoutPlannerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(workoutViewModel = workoutViewModel)
                }
            }
        }
    }
}