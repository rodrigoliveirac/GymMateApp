package com.rodcollab.gymmateapp.exercises.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.rodcollab.gymmateapp.R
import com.rodcollab.gymmateapp.core.data.model.ExerciseExternal
import com.rodcollab.gymmateapp.core.ui.BasicLoading
import com.rodcollab.gymmateapp.exercises.presentation.ExerciseDetailsVm
import com.rodcollab.gymmateapp.exercises.presentation.intent.ExerciseDetailsUiAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetails(
    exerciseDeleted: (String) -> Unit,
    exerciseUpdated:ExerciseExternal?,
    viewModel: ExerciseDetailsVm = hiltViewModel(),
    navigateUp: () -> Unit,
    goTo:(String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var exerciseImageIsLoading by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        exerciseUpdated?.let { exercise ->
            viewModel.toAction(ExerciseDetailsUiAction.OnUpdate(exercise))
        }
    }
    if (uiState.isLoading) {
        BasicLoading(title = "Deleting")
    }

    if(uiState.openDialog) {
        AlertDialog(
            title = {
                    Text(text = "Delete exercise")
            },
            text = {
                Text(text = "This action will delete this exercise forever. Are you sure that you want to do that?")
            },
            onDismissRequest = { viewModel.toAction(ExerciseDetailsUiAction.OnDeleteConfirmation(confirm = false))  },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.toAction(ExerciseDetailsUiAction.OnDeleteConfirmation(confirm = false))
                }) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.toAction(ExerciseDetailsUiAction.OnDeleteConfirmation(confirm = true) {
                            exerciseDeleted(it)
                    })
                }) {
                    Text(text = "Confirm")
                }
            }
        )
    }
    Scaffold(topBar = {
        TopAppBar(modifier = Modifier.shadow(elevation = 6.dp), navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }, title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(1f), text = "Exercise Details")
                if(uiState.isUserExercise) {
                    IconButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = { viewModel.toAction(ExerciseDetailsUiAction.OnEdit, goTo) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.size(4.dp))
                    IconButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = { viewModel.toAction(ExerciseDetailsUiAction.OnDelete, goTo) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Box(Modifier.align(Alignment.CenterHorizontally)) {

                    AsyncImage(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .graphicsLayer {
                                compositingStrategy = CompositingStrategy.Offscreen
                            },
                        model = ImageRequest.Builder(context)
                            .data(uiState.exerciseExternal?.image ?: R.drawable.photo_placeholder)
                            .crossfade(true)
                            .build(),
                        contentDescription = "icon",
                        contentScale = ContentScale.FillBounds,
                        onState = {
                            exerciseImageIsLoading = when (it) {
                                is AsyncImagePainter.State.Loading -> {
                                    true
                                }

                                is AsyncImagePainter.State.Success -> {
                                    false
                                }

                                else -> {

                                    false

                                }
                            }
                        }
                    )

                    if (exerciseImageIsLoading) {
                        CircularProgressIndicator(
                            Modifier
                                .align(Alignment.Center),
                            strokeWidth = 2.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    style = MaterialTheme.typography.headlineMedium,
                    text = "Body Part  ${uiState.exerciseExternal?.bodyPart}"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = "${uiState.exerciseExternal?.name}"
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "${uiState.exerciseExternal?.notes}"
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}