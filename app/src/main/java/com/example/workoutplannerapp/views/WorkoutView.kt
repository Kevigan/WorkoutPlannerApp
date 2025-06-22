package com.example.workoutplannerapp.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.workoutplannerapp.R
import com.example.workoutplannerapp.Screen
import com.example.workoutplannerapp.data.WorkoutItemEntity
import com.example.workoutplannerapp.data.WorkoutItemType
import com.example.workoutplannerapp.data.WorkoutMode
import com.example.workoutplannerapp.data.WorkoutWithItems
import com.example.workoutplannerapp.data.predefinedExercises
import com.example.workoutplannerapp.viewmodels.WorkoutViewModel

@Composable
fun WorkoutView(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    workout: WorkoutWithItems,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = com.example.workoutplannerapp.R.drawable.main_background),
            contentDescription = "Workout Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.1f), // overlay effect
            topBar = {
                TopAppBar(
                    title = { Text(workout.workout.title) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    Button(onClick = {
                        if (workout.workout.mode == WorkoutMode.MANUAL) {
                            Log.d("WorkoutNavigation", "Navigating to PlayView (Manual mode)")
                            navController.navigate(Screen.PlayScreen.route)
                        } else {
                            Log.d("WorkoutNavigation", "Navigating to TimedPlayView (Timed mode)")
                            navController.navigate(Screen.TimedPlayScreen.route)
                        }
                    }) {
                        Text("Start Workout")
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            workoutViewModel.selectWorkout(workout)
                            navController.navigate(Screen.EditWorkoutScreen.route)
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Edit")
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                workout.items.forEach {
                    Log.d("WorkoutItemLogging", "Item: ${it.type} ${it.name ?: "Pause"}")
                }
                items(workout.items) { item ->
                    WorkoutItemEntityRow(item)
                }
            }
        }
    }
}

@Composable
fun WorkoutItemEntityRow(item: WorkoutItemEntity) {
    val imageResId = getImageForExercise(item.name)

    Card(
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageModifier = Modifier
                .padding(end = 12.dp) // Padding on the outside
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)

            // Show image if found
            if (item.type == WorkoutItemType.EXERCISE && imageResId != null) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = item.name,
                    modifier = imageModifier
                )
            } else if (item.type == WorkoutItemType.PAUSE) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_pause_circle_24),
                    contentDescription = "Pause",
                    modifier = imageModifier
                )
            }

            val text = when (item.type) {
                WorkoutItemType.EXERCISE -> {
                    val name = item.name ?: "Exercise"
                    when {
                        item.sets != null && item.reps != null -> "$name: ${item.sets} x ${item.reps} reps"
                        item.durationSeconds != null -> "$name: ${item.durationSeconds} sec"
                        else -> name
                    }
                }

                WorkoutItemType.PAUSE -> {
                    val seconds = item.durationSeconds ?: 0
                    "Pause: $seconds second${if (seconds != 1) "s" else ""}"
                }
            }


            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


fun getImageForExercise(name: String?): Int? {
    return predefinedExercises.firstOrNull { it.name == name }?.imageResId
}


