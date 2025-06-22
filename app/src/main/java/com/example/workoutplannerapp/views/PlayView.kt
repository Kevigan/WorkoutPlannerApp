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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.workoutplannerapp.data.WorkoutItemEntity
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
            GradientExerciseBox(
                item = previousItem,
                label = "Previous",
                small = true,
                gradientColors = listOf(Color(0xFFE0E0E0), Color(0xFFBBDEFB)),
                modifier = Modifier.weight(1f)
            )

            Divider()

            GradientExerciseBox(
                item = currentItem,
                label = "Now",
                small = false,
                gradientColors = listOf(Color(0xFFBBDEFB), Color(0xFF90CAF9)),
                elevated = true,
                modifier = Modifier.weight(2.2f)
            )

            Divider()

            GradientExerciseBox(
                item = nextItem,
                label = "Next",
                small = true,
                gradientColors = listOf(Color(0xFFB2EBF2), Color(0xFFA5D6A7)),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { if (currentIndex > 0) currentIndex-- },
                    enabled = currentIndex > 0
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = { if (currentIndex < workout.items.lastIndex) currentIndex++ },
                    enabled = currentIndex < workout.items.lastIndex
                ) {
                    Text("Next")
                }
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

        val imageModifier = Modifier
            .size(if (small) 64.dp else 240.dp)
            .clip(CircleShape)
            .background(Color.White)

        if (imageResId != null) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                modifier = imageModifier
            )
        } else if (title.contains("Pause", ignoreCase = true)) {
            // Show pause icon if this is a pause and no image was found
            Image(
                painter = painterResource(id = com.example.workoutplannerapp.R.drawable.baseline_pause_circle_24),
                contentDescription = "Pause",
                modifier = imageModifier
            )
        }
    }
}


@Composable
fun GradientExerciseBox(
    item: WorkoutItemEntity?,
    label: String,
    small: Boolean,
    gradientColors: List<Color>,
    elevated: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(if (elevated) 8.dp else 4.dp, shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(brush = Brush.verticalGradient(colors = gradientColors)),
        contentAlignment = Alignment.Center
    ) {
        item?.let {
            ExercisePreview(
                title = "$label: ${it.name}",
                imageResId = getImageForExercise(it.name),
                small = small
            )
        }
    }
}



