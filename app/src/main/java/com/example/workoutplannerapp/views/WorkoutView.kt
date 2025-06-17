package com.example.workoutplannerapp.views

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.workoutplannerapp.data.WorkoutItemEntity
import com.example.workoutplannerapp.data.WorkoutItemType
import com.example.workoutplannerapp.data.WorkoutWithItems
import com.example.workoutplannerapp.data.predefinedExercises

@Composable
fun WorkoutView(
    workout: WorkoutWithItems,
    onBack: () -> Unit
) {
//TODO start workout with timer, or without timer. with timer each exercise shall remove itself after its
    //time is up, if no timer, the user can swipe it to go away.

    Scaffold(
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
                // You can add actions or info here
                Text("Bottom Bar", modifier = Modifier.padding(16.dp))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(workout.items) { item ->
                WorkoutItemEntityRow(item)
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
            // Show image if found
            if (item.type == WorkoutItemType.EXERCISE && imageResId != null) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(end = 12.dp)
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


