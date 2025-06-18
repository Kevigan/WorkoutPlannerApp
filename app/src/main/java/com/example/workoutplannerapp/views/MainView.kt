package com.example.workoutplannerapp.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.workoutplannerapp.R
import com.example.workoutplannerapp.Screen
import com.example.workoutplannerapp.data.WorkoutEntity
import com.example.workoutplannerapp.viewmodels.WorkoutViewModel

@Composable
fun MainView(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
) {
    val workoutList by workoutViewModel.allWorkouts.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Foreground UI
        Scaffold(
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.1f), // Optional overlay
            topBar = {
                TopAppBar(title = { Text("Workout Planner") })
            },
            bottomBar = {
                BottomAppBar {
                    Text("Bottom Bar", modifier = Modifier.padding(16.dp))
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    navController.navigate(Screen.CreateWorkoutScreen.route)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Workout")
                }
            }
        ) { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.primary.copy(alpha = 0.7f))
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "My Workouts",
                            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Divider(
                        color = Color.Gray.copy(alpha = 0.5f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                items(workoutList) { workoutWithItems ->
                    WorkoutItem(
                        workout = workoutWithItems.workout,
                        onClick = {
                            workoutViewModel.selectWorkout(workoutWithItems)
                            navController.navigate(Screen.WorkoutScreen.route)
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun WorkoutItem(workout: WorkoutEntity, onClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                color = Color.White,
                shape = MaterialTheme.shapes.medium
            ),
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFA5D6A7), // Light Green 300
                            Color(0xFF81C784)  // Light Green 400
                        )
                    )
                )
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = workout.title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20) // Darker green text for contrast
                )
            )
        }
    }
}



