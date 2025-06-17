package com.example.workoutplannerapp.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workoutplannerapp.data.WorkoutWithItems
import kotlinx.coroutines.delay

@Composable
fun TimedPlayView(
    workout: WorkoutWithItems,
    onBack: () -> Unit
) {
    var hasStarted by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(0) }

    val currentItem = workout.items.getOrNull(currentIndex)
    val previousItem = workout.items.getOrNull(currentIndex - 1)
    val nextItem = workout.items.getOrNull(currentIndex + 1)

    // Countdown logic
    LaunchedEffect(key1 = hasStarted, key2 = currentIndex) {
        if (hasStarted && currentItem != null && currentItem.durationSeconds != null) {
            timeLeft = currentItem.durationSeconds
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            // Auto-progress to next
            if (currentIndex < workout.items.lastIndex) {
                currentIndex++
            } else {
                hasStarted = false // Finished
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guided Workout: ${workout.workout.title}") },
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
            // Previous
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

            // Current
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2.2f),
                contentAlignment = Alignment.Center
            ) {
                if (!hasStarted) {
                    Button(onClick = { hasStarted = true }) {
                        Text("Start Workout")
                    }
                } else {
                    currentItem?.let {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ExercisePreview(
                                title = "Now: ${it.name}",
                                imageResId = getImageForExercise(it.name),
                                small = false
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Time left: $timeLeft sec",
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                }
            }

            Divider()

            // Next
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
        }
    }
}
