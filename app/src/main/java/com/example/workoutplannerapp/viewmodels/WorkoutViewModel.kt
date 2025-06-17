package com.example.workoutplannerapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.workoutplannerapp.data.AppDatabase
import com.example.workoutplannerapp.data.WorkoutItemEntity
import com.example.workoutplannerapp.data.WorkoutWithItems
import com.example.workoutplannerapp.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WorkoutRepository
    val allWorkouts: LiveData<List<WorkoutWithItems>>

    private val _selectedWorkout = MutableLiveData<WorkoutWithItems?>()
    val selectedWorkout: LiveData<WorkoutWithItems?> = _selectedWorkout

    init {
        val dao = AppDatabase.getInstance(application).workoutDao()
        repository = WorkoutRepository(dao)
        allWorkouts = repository.allWorkouts.asLiveData()
    }

    fun saveWorkout(title: String, items: List<WorkoutItemEntity>) {
        viewModelScope.launch {
            repository.insertWorkoutWithItems(title, items)
        }
    }

    fun selectWorkout(workout: WorkoutWithItems) {
        _selectedWorkout.value = workout
    }

    fun clearSelectedWorkout() {
        _selectedWorkout.value = null
    }
}


