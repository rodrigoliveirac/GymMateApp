package com.rodcollab.gymmateapp.exercises.presentation.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.addOrEditArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.bodyPartArgs
import com.rodcollab.gymmateapp.core.navigation.GymMateDestinationsArgs.exerciseIdArgs
import com.rodcollab.gymmateapp.core.AddOrEdit
import com.rodcollab.gymmateapp.core.FileUtils
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
import com.rodcollab.gymmateapp.exercises.presentation.intent.AddOrEditExerciseUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

data class AddOrEditExerciseUiState(
    val name: String = "",
    val bodyPart: String = "",
    val imgUrl: String? = null,
    val notes: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val addOrEditTitle: String = "",
    val newPhotoIsLoading: Boolean = false
)

@HiltViewModel
class AddOrEditExerciseVm @Inject constructor(
    private val domain: ExercisesDomain,
    savedStateHandle: SavedStateHandle,
    private val application: Application
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(AddOrEditExerciseUiState())
    val uiState = _uiState.asStateFlow()

    private val bodyPart = checkNotNull(savedStateHandle[bodyPartArgs])
    private val addOrEdit = checkNotNull(savedStateHandle[addOrEditArgs])

    private val exerciseId: String? = savedStateHandle[exerciseIdArgs]

    init {
        val exercise = exerciseId?.let { id ->
            domain.readExercise(id)
        } ?: ExerciseExternal()
        _uiState.update {
            it.copy(
                name = exercise.name ?: "",
                imgUrl = exercise.image,
                notes = exercise.notes ?: "",
                bodyPart = bodyPart as String,
                addOrEditTitle = if (addOrEdit == AddOrEdit.ADD.name) "New exercise" else "Edit exercise"
            )
        }
    }

    fun onAddOrEditUiAction(action: AddOrEditExerciseUiAction, navigateUp: (ExerciseExternal?) -> Unit = { }) {
        viewModelScope.launch {
            when (action) {
                is AddOrEditExerciseUiAction.OnNameChange -> {
                    _uiState.update {
                        it.copy(name = action.name)
                    }
                }

                is AddOrEditExerciseUiAction.OnImgBitmapChange -> {

                    withContext(Dispatchers.Main) {
                        _uiState.update { it.copy(newPhotoIsLoading = true) }
                    }

                    val uri: String? = getUri(action.data)

                    _uiState.update {
                        it.copy(newPhotoIsLoading = false, imgUrl = Uri.parse(uri).toString())
                    }
                }

                is AddOrEditExerciseUiAction.OnNotesChange -> {
                    _uiState.update {
                        it.copy(notes = action.notes)
                    }
                }

                is AddOrEditExerciseUiAction.OnDelete -> {
                }

                is AddOrEditExerciseUiAction.OnConfirm -> {
                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(isLoading = true, message = "Loading")
                        }
                    }
                    domain.addOrEditExercise(
                        bodyPart = _uiState.value.bodyPart,
                        document = exerciseId,
                        name = _uiState.value.name,
                        img = _uiState.value.imgUrl,
                        notes = _uiState.value.notes
                    ) { resultOf ->
                        when (resultOf) {
                            is ResultOf.Success -> {
                                withContext(Dispatchers.Main) {
                                    _uiState.update {
                                        it.copy(isLoading = false, message = "Exercise Saved")
                                    }
                                    _uiState.update {
                                        it.copy(message = null)
                                    }
                                    navigateUp(resultOf.value)
                                }
                            }

                            is ResultOf.Failure -> {
                                withContext(Dispatchers.Main) {
                                    _uiState.update {
                                        it.copy(isLoading = false, message = resultOf.message)
                                    }
                                    _uiState.update {
                                        it.copy(message = null)
                                    }
                                }

                            }

                            else -> {

                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun getUri(data: Any?): String? {
        return withContext(Dispatchers.IO) {
            when (data) {
                is Bitmap -> {
                    data.let { bitmap ->
                        FileUtils.saveBitmapToFile(
                            application,
                            bitmap,
                            "${_uiState.value.bodyPart}:${_uiState.value.name}:${
                                UUID.randomUUID().toString()
                            }"
                        )
                    }
                }

                else -> {
                    data?.toString()
                }
            }
        }
    }
}

