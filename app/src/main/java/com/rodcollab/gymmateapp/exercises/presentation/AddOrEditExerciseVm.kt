package com.rodcollab.gymmateapp.exercises.presentation

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.addOrEditArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.bodyPartArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.exerciseIdArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.imgUrlExerciseArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.nameExerciseArgs
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinationsArgs.notesExerciseArgs
import com.rodcollab.gymmateapp.core.AddOrEdit
import com.rodcollab.gymmateapp.core.FileUtils
import com.rodcollab.gymmateapp.core.ResultOf
import com.rodcollab.gymmateapp.exercises.domain.model.ExercisesDomain
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
    private val name = savedStateHandle[nameExerciseArgs] ?: ""
    private val imgUrl: String? = savedStateHandle[imgUrlExerciseArgs]
    private val notes = savedStateHandle[notesExerciseArgs] ?: ""

    init {
        _uiState.update {
            it.copy(
                name = name,
                imgUrl = imgUrl,
                notes = notes,
                bodyPart = bodyPart as String,
                addOrEditTitle = if (addOrEdit == AddOrEdit.ADD.name) "New exercise" else "Edit exercise"
            )
        }
    }

    fun onAddOrEditUiAction(action: AddOrEditExerciseUiAction,navigateUp: ()-> Unit = { }) {
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
                    _uiState.update {
                        it.copy(isLoading = true, message = "Loading")
                    }
                    domain.addExercise(
                        bodyPart = _uiState.value.bodyPart,
                        document = exerciseId,
                        name = _uiState.value.name,
                        img = _uiState.value.imgUrl,
                        notes = _uiState.value.notes
                    ) { resultOf ->
                        when (resultOf) {
                            is ResultOf.Success -> {
                                _uiState.update {
                                    it.copy(isLoading = false, message = "Exercise Saved")
                                }
                                _uiState.update {
                                    it.copy(message = null)
                                }
                                navigateUp()
                            }
                            is ResultOf.Failure -> {
                                _uiState.update {
                                    it.copy(isLoading = false, message = resultOf.message)
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
                            "${_uiState.value.bodyPart}:${_uiState.value.name}:${UUID.randomUUID().toString()}"
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

sealed interface AddOrEditExerciseUiAction {
    data class OnNameChange(val name: String) : AddOrEditExerciseUiAction
    data class OnImgBitmapChange(val data: Any?, val uploadFile: (String) -> Unit) : AddOrEditExerciseUiAction
    data class OnNotesChange(val notes: String) : AddOrEditExerciseUiAction
    data object OnDelete : AddOrEditExerciseUiAction
    data object OnConfirm : AddOrEditExerciseUiAction
}

