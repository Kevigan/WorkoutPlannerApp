package com.example.workoutplannerapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.workoutplannerapp.data.WorkoutMode
import com.example.workoutplannerapp.data.WorkoutWithItems

@Composable
fun PlayView(
    workout: WorkoutWithItems,
    onBack: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    val previousItem = workout.items.getOrNull(currentIndex - 1)
    val currentItem = workout.items.getOrNull(currentIndex)
    val nextItem = workout.items.getOrNull(currentIndex + 1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout: ${workout.workout.title}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Previous (small)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                previousItem?.let {
                    ExercisePreview(
                        title = "Previous: ${it.name}",
                        imageResId = getImageForExercise(it.name),
                        small = true
                    )
                }
            }

            Divider()

            // Current (larger)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2.2f), // Larger than previous and next
                contentAlignment = Alignment.Center
            ) {
                currentItem?.let {
                    ExercisePreview(
                        title = "Now: ${it.name}",
                        imageResId = getImageForExercise(it.name),
                        small = false
                    )
                }
            }

            Divider()

            // Next (small)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                nextItem?.let {
                    ExercisePreview(
                        title = "Next: ${it.name}",
                        imageResId = getImageForExercise(it.name),
                        small = true
                    )
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { if (currentIndex > 0) currentIndex-- },
                    enabled = currentIndex > 0
                ) { Text("Previous") }

                Button(
                    onClick = { if (currentIndex < workout.items.lastIndex) currentIndex++ },
                    enabled = currentIndex < workout.items.lastIndex
                ) { Text("Next") }
            }
        }
    }
}

@Composable
fun ExercisePreview(title: String, imageResId: Int?, small: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = title,
            style = if (small) MaterialTheme.typography.body2 else MaterialTheme.typography.h6
        )
        imageResId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = title,
                modifier = Modifier
                    .size(if (small) 64.dp else 240.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
        }
    }
}



