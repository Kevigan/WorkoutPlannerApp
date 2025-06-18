package com.example.workoutplannerapp.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.workoutplannerapp.R
import com.example.workoutplannerapp.data.WorkoutItemEntity
import com.example.workoutplannerapp.data.WorkoutItemType
import com.example.workoutplannerapp.data.predefinedExercises
import com.example.workoutplannerapp.viewmodels.WorkoutViewModel
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.workoutplannerapp.Screen
import com.example.workoutplannerapp.data.ExerciseCategory
import com.example.workoutplannerapp.data.ExerciseDefinition
import com.example.workoutplannerapp.data.SelectedExercise
import com.example.workoutplannerapp.data.WorkoutMode
import com.example.workoutplannerapp.data.WorkoutWithItems


@Composable
fun CreateWorkoutView(
    navController: NavController,
    workoutViewModel: WorkoutViewModel,
    workoutToEdit: WorkoutWithItems? = null,
    isEditing: Boolean = false
) {
    val selectedExercises = remember { mutableStateListOf<SelectedExercise>() }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val listState = remember { LazyListState() }
    var showSaveDialog by remember { mutableStateOf(false) }
    var workoutName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ExerciseCategory?>(null) }
    var workoutMode by remember { mutableStateOf(WorkoutMode.MANUAL) }

    LaunchedEffect(workoutToEdit) {
        workoutToEdit?.let { workout ->
            workoutName = workout.workout.title
            workoutMode = workout.workout.mode
            selectedExercises.clear()
            selectedExercises.addAll(
                workout.items.map { item ->
                    val definition = predefinedExercises.find { it.name == item.name }
                        ?: ExerciseDefinition(
                            name = item.name ?: "Unknown",
                            category = ExerciseCategory.PAUSE,
                            imageResId = R.drawable.baseline_pause_circle_24
                        )

                    SelectedExercise(
                        definition = definition,
                        sets = mutableStateOf(item.sets ?: 3),
                        reps = mutableStateOf(item.reps ?: 12),
                        durationSeconds = mutableStateOf(item.durationSeconds ?: 30)
                    )
                }
            )
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = "Workout Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Scaffold(
            backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.1f),
            topBar = {
                TopAppBar(title = { Text("Create Workout") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE3F2FD), shape = MaterialTheme.shapes.medium)
                        .padding(12.dp)
                ) {
                    Column {
                        Text("Workout Mode", style = MaterialTheme.typography.h6)

                        Row(
                            modifier = Modifier.padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            WorkoutMode.values().forEach { mode ->
                                val isSelected = workoutMode == mode
                                OutlinedButton(
                                    onClick = { workoutMode = mode },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        backgroundColor = if (isSelected) Color(0xFF90CAF9) else Color.Transparent,
                                        contentColor = if (isSelected) Color.White else MaterialTheme.colors.onSurface
                                    )
                                ) {
                                    Text(mode.name.lowercase().replaceFirstChar { it.uppercase() })
                                }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colors.surface.copy(alpha = 0.85f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "Available Exercises",
                            modifier = Modifier.padding(bottom = 4.dp),
                            style = MaterialTheme.typography.h6
                        )

                        CategorySelectorRow(
                            selectedCategory = selectedCategory,
                            onCategorySelected = { category ->
                                if (category == ExerciseCategory.PAUSE) {
                                    selectedExercises.add(
                                        SelectedExercise(
                                            definition = ExerciseDefinition(
                                                name = "Pause",
                                                category = ExerciseCategory.PAUSE,
                                                imageResId = R.drawable.baseline_pause_circle_24
                                            )
                                        )
                                    )
                                } else {
                                    selectedCategory = category
                                }
                            }
                        )


                        if (selectedCategory != null) {
                            val filteredExercises = predefinedExercises.filter { it.category == selectedCategory }

                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(filteredExercises.size) { index ->
                                    val exercise = filteredExercises[index]
                                    Box(
                                        modifier = Modifier
                                            .clickable { selectedExercises.add(SelectedExercise(definition = exercise)) }
                                            .background(
                                                color = Color(0xFFE3F2FD),
                                                shape = MaterialTheme.shapes.medium
                                            )
                                            .border(
                                                1.dp, Color(0xFF90CAF9),
                                                shape = MaterialTheme.shapes.medium
                                            )
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = exercise.name,
                                            style = MaterialTheme.typography.body1.copy(color = Color.Black)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }


                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colors.surface.copy(alpha = 0.85f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = "Selected Exercises",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    state = listState
                ) {
                    itemsIndexed(selectedExercises) { index, exercise ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedIndex = index }
                                .background(
                                    color = if (selectedIndex == index) Color(0xFFBBDEFB) else Color(0xFFE3F2FD),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selectedIndex == index) Color.Blue else Color(0xFF90CAF9),
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = exercise.definition.imageResId),
                                        contentDescription = exercise.definition.name,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .padding(end = 12.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                    )

                                    Text(
                                        text = "${index + 1}. ${exercise.definition.name}",
                                        style = MaterialTheme.typography.body1.copy(color = Color.Black)
                                    )
                                }

                                // Sets x Reps or Duration
                                val detailsText = if (
                                    workoutMode == WorkoutMode.TIMED ||
                                    exercise.definition.category == ExerciseCategory.CARDIO ||
                                    exercise.definition.category == ExerciseCategory.PAUSE
                                ) {
                                    "${exercise.durationSeconds.value}s"
                                } else {
                                    "${exercise.sets.value}x${exercise.reps.value}"
                                }


                                Text(
                                    text = detailsText,
                                    style = MaterialTheme.typography.body2.copy(color = Color.DarkGray)
                                )
                            }

                        }
                    }
                }

                Button(
                    onClick = { showSaveDialog = true },
                    enabled = selectedExercises.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Save Workout")
                }

                if (showSaveDialog) {
                    SaveWorkoutDialog(
                        workoutName = workoutName,
                        onNameChange = { workoutName = it },
                        onDismiss = {
                            showSaveDialog = false
                            workoutName = ""
                        },
                        onSave = {
                            // Convert ExerciseDefinition to WorkoutItemEntity
                            val items = selectedExercises.map { selected ->
                                val isDurationBased = workoutMode == WorkoutMode.TIMED ||
                                        selected.definition.category == ExerciseCategory.CARDIO ||
                                        selected.definition.category == ExerciseCategory.PAUSE


                                WorkoutItemEntity(
                                    workoutId = 0,
                                    type = WorkoutItemType.EXERCISE,
                                    name = selected.definition.name,
                                    reps = if (isDurationBased) null else selected.reps.value,
                                    sets = if (isDurationBased) null else selected.sets.value,
                                    durationSeconds = if (isDurationBased) (selected.durationSeconds.value).coerceAtLeast(1) else null
                                )
                            }
                            if (isEditing && workoutToEdit != null) {
                                workoutViewModel.updateWorkout(
                                    workoutToEdit.workout.copy(title = workoutName, mode = workoutMode),
                                    items
                                )
                            }
                            else {
                                workoutViewModel.saveWorkoutWithMode(title = workoutName, mode = workoutMode, items = items)
                            }

                            // Clear dialog and input state
                            showSaveDialog = false
                            workoutName = ""
                            selectedExercises.clear()
                            selectedIndex = null

                            navController.navigate(Screen.MainScreen.route) {
                                popUpTo(Screen.MainScreen.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }

                        }
                    )
                }
            }
        }

        selectedIndex?.let { index ->
            BackHandler(enabled = true) {
                selectedIndex = null
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Blocking overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable(enabled = true, onClick = {})
                )

                // Set & Reps Card (Bottom Center, shifted left)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(start = 8.dp, end = 80.dp, bottom = 8.dp) // <-- reserve right space
                ) {
                    SetRepsAndSetsCard(
                        exercise = selectedExercises[index],
                        onDelete = {
                            selectedExercises.removeAt(index)
                            selectedIndex = null
                        },
                        workoutMode = workoutMode
                    )
                }

                // Reorder Panel (Bottom Right)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 12.dp)
                ) {
                    ReorderButtonsPanel(
                        index = index,
                        exercises = selectedExercises,
                        onIndexChange = { newIndex -> selectedIndex = newIndex },
                        listState = listState
                    )
                }
            }
        }
    }
}

@Composable
fun SetRepsAndSetsCard(
    exercise: SelectedExercise,
    onDelete: () -> Unit,
    workoutMode: WorkoutMode
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFBBDEFB))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (workoutMode == WorkoutMode.TIMED ||
                        exercise.definition.category == ExerciseCategory.CARDIO ||
                        exercise.definition.category == ExerciseCategory.PAUSE
                    ) {
                        // ⏱️ Show duration-based UI
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Duration", style = MaterialTheme.typography.body2)
                            IconButton(onClick = {
                                if (exercise.durationSeconds.value > 10) exercise.durationSeconds.value -= 10
                            }) {
                                Icon(
                                    painterResource(R.drawable.baseline_remove_circle_24),
                                    contentDescription = "Dec Duration",
                                    tint = Color.Black
                                )
                            }
                            Text("${exercise.durationSeconds.value} sec", modifier = Modifier.padding(horizontal = 4.dp))
                            IconButton(onClick = {
                                exercise.durationSeconds.value += 10
                            }) {
                                Icon(
                                    painterResource(R.drawable.baseline_add_circle_24),
                                    contentDescription = "Inc Duration",
                                    tint = Color.Black
                                )
                            }
                        }
                    } else {
                        // 💪 Show sets & reps-based UI
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Sets", style = MaterialTheme.typography.body2)
                            IconButton(onClick = {
                                if (exercise.sets.value > 1) exercise.sets.value--
                            }) {
                                Icon(
                                    painterResource(R.drawable.baseline_remove_circle_24),
                                    contentDescription = "Dec Sets",
                                    tint = Color.Black
                                )
                            }
                            Text("${exercise.sets.value}", modifier = Modifier.padding(horizontal = 4.dp))
                            IconButton(onClick = {
                                exercise.sets.value++
                            }) {
                                Icon(
                                    painterResource(R.drawable.baseline_add_circle_24),
                                    contentDescription = "Inc Sets",
                                    tint = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Reps", style = MaterialTheme.typography.body2)
                            IconButton(onClick = {
                                if (exercise.reps.value > 1) exercise.reps.value--
                            }) {
                                Icon(
                                    painterResource(R.drawable.baseline_remove_circle_24),
                                    contentDescription = "Dec Reps",
                                    tint = Color.Black
                                )
                            }
                            Text("${exercise.reps.value}", modifier = Modifier.padding(horizontal = 4.dp))
                            IconButton(onClick = {
                                exercise.reps.value++
                            }) {
                                Icon(
                                    painterResource(R.drawable.baseline_add_circle_24),
                                    contentDescription = "Inc Reps",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun ReorderButtonsPanel(
    index: Int,
    exercises: SnapshotStateList<SelectedExercise>,
    onIndexChange: (Int) -> Unit,
    listState: LazyListState
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (index > 0) {
                    exercises.removeAt(index).also {
                        exercises.add(index - 1, it)
                        onIndexChange(index - 1)
                        coroutineScope.launch {
                            listState.animateScrollToItem(index - 1)
                        }
                    }
                }
            },
            enabled = index > 0
        ) {
            Text("↑")
        }

        Button(
            onClick = {
                if (index < exercises.lastIndex) {
                    exercises.removeAt(index).also {
                        exercises.add(index + 1, it)
                        onIndexChange(index + 1)
                        coroutineScope.launch {
                            listState.animateScrollToItem(index + 1)
                        }
                    }
                }
            },
            enabled = index < exercises.lastIndex
        ) {
            Text("↓")
        }
    }
}


@Composable
fun SaveWorkoutDialog(
    workoutName: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save Workout") },
        text = {
            Column {
                Text("Enter a name for your workout:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = workoutName,
                    onValueChange = onNameChange,
                    label = { Text("Workout Name") },
                    singleLine = true,
                    isError = workoutName.isBlank()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onSave,
                enabled = workoutName.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CategorySelectorRow(
    selectedCategory: ExerciseCategory?,
    onCategorySelected: (ExerciseCategory) -> Unit
) {
    val categories = ExerciseCategory.values().toList()

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            val isSelected = category == selectedCategory

            Box(
                modifier = Modifier
                    .clickable { onCategorySelected(category) }
                    .background(
                        color = if (isSelected) Color(0xFF90CAF9) else Color(0xFFE3F2FD),
                        shape = MaterialTheme.shapes.medium
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.Blue else Color.Gray,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.body2.copy(color = Color.Black)
                )
            }
        }
    }
}


