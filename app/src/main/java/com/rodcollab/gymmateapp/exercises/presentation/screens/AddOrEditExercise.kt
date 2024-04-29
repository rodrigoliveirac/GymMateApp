package com.rodcollab.gymmateapp.exercises.presentation.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.rodcollab.gymmateapp.R
import com.rodcollab.gymmateapp.core.ui.BasicLoading
import com.rodcollab.gymmateapp.core.ui.WidgetDialog
import com.rodcollab.gymmateapp.exercises.presentation.AddOrEditExerciseUiAction
import com.rodcollab.gymmateapp.exercises.presentation.AddOrEditExerciseVm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditExerciseScreen(
    navigateUp: () -> Unit,
    viewModel: AddOrEditExerciseVm = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val launcherTakePicture =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                viewModel.onAddOrEditUiAction(AddOrEditExerciseUiAction.OnImgBitmapChange(data = it) { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                })
            }
            hideBottomSheet(scope, sheetState) { showBottomSheet = it }
        }
    val launcherPickVisualMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                viewModel.onAddOrEditUiAction(AddOrEditExerciseUiAction.OnImgBitmapChange(data = it) { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                })
            }
            hideBottomSheet(scope, sheetState) { showBottomSheet = it }
        }

    var exerciseImageIsLoading by remember {
        mutableStateOf(false)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            onDismissRequest = {
                showBottomSheet = !showBottomSheet
            },
            sheetState = sheetState
        ) {
            Row(
                modifier = Modifier.padding(start = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall,
                    text = "Exercise Photo"
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    Modifier.clickable { launcherTakePicture.launch() },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.border(
                            1.dp,
                            Color.LightGray.copy(alpha = 0.4f),
                            CircleShape
                        )
                    ) {
                        Icon(
                            tint = Color(0xFFFFB1C8),
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.Center),
                            painter = painterResource(id = R.drawable.icon_camera),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.SansSerif,
                        style = MaterialTheme.typography.titleMedium,
                        text = "Camera"
                    )
                }
                Spacer(modifier = Modifier.size(24.dp))
                Column(Modifier.clickable {
                    launcherPickVisualMedia.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.border(
                            1.dp,
                            Color.LightGray.copy(alpha = 0.4f),
                            CircleShape
                        )
                    ) {
                        Icon(
                            tint = Color(0xFFFFB1C8),
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.Center),
                            painter = painterResource(id = R.drawable.icon_gallery),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.SansSerif,
                        text = "Gallery"
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Image(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = { Text(text = uiState.addOrEditTitle) })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxSize()) {
                Box(Modifier.align(Alignment.CenterHorizontally)) {

                    AsyncImage(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .graphicsLayer {
                                compositingStrategy = CompositingStrategy.Offscreen
                            },
                        model = ImageRequest.Builder(context)
                            .data(uiState.imgUrl ?: R.drawable.photo_placeholder)
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

                    Button(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp),
                        shape = CircleShape,

                        contentPadding = PaddingValues(0.dp),
                        onClick = { showBottomSheet = !showBottomSheet }) {
                        Icon(
                            modifier = Modifier.padding(8.dp),
                            tint = Color.White,
                            painter = painterResource(id = R.drawable.icon_camera),
                            contentDescription = null
                        )
                    }

                    if (uiState.newPhotoIsLoading || exerciseImageIsLoading) {
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
                    text = "Body Part  ${uiState.bodyPart}"
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.name, onValueChange = {
                        viewModel.onAddOrEditUiAction(AddOrEditExerciseUiAction.OnNameChange(name = it))
                    })
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.notes, onValueChange = {
                        viewModel.onAddOrEditUiAction(AddOrEditExerciseUiAction.OnNotesChange(notes = it))
                    })
                Spacer(modifier = Modifier.size(8.dp))
                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.onAddOrEditUiAction(AddOrEditExerciseUiAction.OnConfirm) {
                            navigateUp()
                        }
                    }) {
                    Text(text = "Confirm")
                }
            }
        }
    }
    if(uiState.isLoading) {
        uiState.message?.let {
            BasicLoading(title = it)
        }
    }
    uiState.message?.let {
        WidgetDialog {
            Text(text = it)
            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun hideBottomSheet(
    scope: CoroutineScope,
    sheetState: SheetState,
    showBottomSheet: (Boolean) -> Unit
) {
    scope.launch { sheetState.hide() }.invokeOnCompletion {
        if (!sheetState.isVisible) {
            showBottomSheet(false)
        }
    }
}