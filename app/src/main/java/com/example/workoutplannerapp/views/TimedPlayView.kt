package com.example.workoutplannerapp.views

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.workoutplannerapp.data.WorkoutWithItems
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import com.example.workoutplannerapp.Screen

@Composable
fun TimedPlayView(
    workout: WorkoutWithItems,
    navController: NavController,
    onBack: () -> Unit
) {
    var hasStarted by remember { mutableStateOf(false) }
    var isPreCountdown by remember { mutableStateOf(false) }
    var preCountdown by remember { mutableStateOf(3) }
    var currentIndex by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(0) }

    val currentItem = workout.items.getOrNull(currentIndex)
    val previousItem = workout.items.getOrNull(currentIndex - 1)
    val nextItem = workout.items.getOrNull(currentIndex + 1)

    var isFinished by remember { mutableStateOf(false) }

    var isPulsating by remember { mutableStateOf(false) }

    val pulseScale by animateFloatAsState(
        targetValue = if (isPulsating) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ), label = "PulseScale"
    )


    // Pre-countdown logic (3...2...1...)
    LaunchedEffect(key1 = isPreCountdown) {
        if (isPreCountdown) {
            for (i in 3 downTo 1) {
                preCountdown = i
                delay(1000)
            }
            isPreCountdown = false
            hasStarted = true
        }
    }

    // Workout timer logic
    LaunchedEffect(key1 = hasStarted, key2 = currentIndex) {
        if (hasStarted && currentItem?.durationSeconds != null) {
            timeLeft = currentItem.durationSeconds
            while (timeLeft > 0) {
                if (timeLeft <= 3) isPulsating = true
                delay(1000)
                timeLeft--
            }
            isPulsating = false

            if (currentIndex < workout.items.lastIndex) {
                currentIndex++
            } else {
                isFinished = true
                hasStarted = false
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
                    .weight(1f)
                    .padding(8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE0E0E0),
                                Color(0xFFBBDEFB)
                            ) // light gray to light blue
                        )
                    ),
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

            // Current (highlighted with shadow and gradient)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2.2f)
                    .padding(8.dp)
                    .graphicsLayer(
                        scaleX = pulseScale,
                        scaleY = pulseScale
                    )
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFBBDEFB), Color(0xFF90CAF9))
                        )
                    ),
                contentAlignment = Alignment.Center
            )
            {
                when {
                    isFinished -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎉 Good job!", style = MaterialTheme.typography.h4)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                isFinished = false
                                currentIndex = 0
                                isPreCountdown = true
                            }) {
                                Text("One More Time")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                navController.navigate(Screen.MainScreen.route) {
                                    popUpTo(Screen.MainScreen.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }) {
                                Text("Quit")
                            }

                        }
                    }
                    isPreCountdown -> {
                        Text("Starting in $preCountdown...", style = MaterialTheme.typography.h4)
                    }
                    !hasStarted -> {
                        Button(onClick = { isPreCountdown = true }) {
                            Text("Start Workout")
                        }
                    }
                    else -> {
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

            }

            Divider()

            // Next
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFB2EBF2),
                                Color(0xFFA5D6A7)
                            ) // teal to light green
                        )
                    ),
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
