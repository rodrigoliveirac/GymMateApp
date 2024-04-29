package com.rodcollab.gymmateapp.exercises.presentation.screens

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.rodcollab.gymmateapp.R
import com.rodcollab.gymmateapp.exercises.presentation.BPExercisesUiAction
import com.rodcollab.gymmateapp.exercises.presentation.BPExercisesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    goTo: (String) -> Unit,
    navigateUp: () -> Unit,
    sharedViewModel: BPExercisesViewModel
) {
    val uiState by sharedViewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { sharedViewModel.onUiActions(BPExercisesUiAction.OnNewExercise) { goTo(it)} }) {
                Image(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.shadow(elevation = 6.dp),
                title = {
                    Text(text = uiState.exercisesTopBarTitle)
                })
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if(uiState.isLoading) {
                Column(Modifier.fillMaxSize().align(Alignment.Center), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Loading")
                    Spacer(modifier = Modifier.size(8.dp))
                    CircularProgressIndicator(strokeWidth = 2.dp)
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp), columns = GridCells.Fixed(2)
                ) {
                    items(uiState.exercisesByBP) { exercise ->
                        val imageLoader = ImageLoader.Builder(context)
                            .components {
                                if (SDK_INT >= 28) {
                                    add(ImageDecoderDecoder.Factory())
                                } else {
                                    add(GifDecoder.Factory())
                                }
                            }
                            .build()
                        Card(
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .padding(8.dp)
                                .clickable { }
                        ) {
                            val dataImg: Any = exercise.image ?: run {
                                R.drawable.photo_placeholder

                            }
                            AsyncImage(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(dataImg)
                                    .build(),
                                imageLoader = imageLoader,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                fontWeight = FontWeight.Light,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                text = exercise.name,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}