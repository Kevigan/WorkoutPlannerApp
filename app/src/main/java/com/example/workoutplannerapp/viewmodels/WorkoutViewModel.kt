package com.example.workoutplannerapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.workoutplannerapp.data.AppDatabase
import com.example.workoutplannerapp.data.WorkoutEntity
import com.example.workoutplannerapp.data.WorkoutItemEntity
import com.example.workoutplannerapp.data.WorkoutMode
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

    fun saveWorkoutWithMode(title: String, mode: WorkoutMode, items: List<WorkoutItemEntity>) {
        viewModelScope.launch {
            repository.insertWorkoutWithItems(title, mode, items)
        }
    }

    fun selectWorkout(workout: WorkoutWithItems) {
        _selectedWorkout.value = workout
    }

    fun updateWorkout(workout: WorkoutEntity, items: List<WorkoutItemEntity>) {
        viewModelScope.launch {
            repository.updateWorkout(workout, items)
        }
    }

    fun clearSelectedWorkout() {
        _selectedWorkout.value = null
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkoutWithItems(workout)
        }
    }

}


