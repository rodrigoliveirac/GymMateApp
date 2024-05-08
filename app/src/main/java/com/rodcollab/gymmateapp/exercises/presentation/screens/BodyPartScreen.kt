package com.rodcollab.gymmateapp.exercises.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.rodcollab.gymmateapp.R
import com.rodcollab.gymmateapp.exercises.presentation.BPExercisesUiAction
import com.rodcollab.gymmateapp.exercises.presentation.BPExercisesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyPartScreen(
    goTo: (String) -> Unit,
    viewModel: BPExercisesViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        bottomBar = {

            BottomAppBar {
                IconButton(
                    onClick = {  }
                ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_event_note_24), contentDescription = "Training")
                }

                IconButton(
                    onClick = { /* Handle navigation icon click */ }
                ) {
                    Icon(painter = painterResource(id = R.drawable.dumbell), contentDescription = "Exercises")
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(elevation = 6.dp),
                title = {
                    Text(text = "Body Parts")
                })
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if(uiState.isLoading) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.Center), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Loading")
                    Spacer(modifier = Modifier.size(8.dp))
                    CircularProgressIndicator(strokeWidth = 2.dp)
                }
            } else {
                LazyColumn {
                    items(uiState.bpWithExercises.keys.toList()) { bodyParty ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            onClick = {
                                viewModel.onUiActions(BPExercisesUiAction.OnBodyPart(bodyParty),goTo)
                            }) {
                            Row(modifier = Modifier.padding(8.dp),verticalAlignment = Alignment.CenterVertically) {
                                Box() {
                                    var isLoading by rememberSaveable { mutableStateOf(false) }
                                    AsyncImage(model = ImageRequest.Builder(context).data(bodyParty.imgUrl).crossfade(true).build(),
                                        contentDescription = "icon",
                                        contentScale = ContentScale.FillBounds,
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(
                                                RoundedCornerShape(28.dp)
                                            ),
                                        onState = {
                                            when (it) {
                                                is AsyncImagePainter.State.Loading -> {
                                                    isLoading = true
                                                }

                                                is AsyncImagePainter.State.Success -> {
                                                    isLoading = false
                                                }

                                                else -> {

                                                }
                                            }
                                        })
                                    if(isLoading) {
                                        CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.align(Alignment.Center))
                                    }
                                }
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, text = bodyParty.name.uppercase())
                            }
                        }
                    }
                }
            }
        }

//        if(uiState.displaySnackbar) {
//            LaunchedEffect(uiState.message) {
//                snackbarHostState.showSnackbar(uiState.message)
//                delay(500)
//                viewModel.hideSnackBar()
//            }
//        }
    }
}