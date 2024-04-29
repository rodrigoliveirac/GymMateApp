package com.rodcollab.gymmateapp.routines

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rodcollab.gymmateapp.R
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateDestinations.ADD_TRAINING_ROUTE
import com.rodcollab.gymmateapp.auth.presentation.navigation.GymMateScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    goTo: (String) -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { goTo(ADD_TRAINING_ROUTE) }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }, bottomBar = {
        BottomAppBar {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_event_note_24),
                    contentDescription = "Training"
                )
            }

            IconButton(onClick = { goTo(GymMateScreens.MAIN_SCREEN) }) {
                Icon(
                    painter = painterResource(id = R.drawable.dumbell),
                    contentDescription = "Exercises"
                )
            }
        }
    },

        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier.shadow(elevation = 6.dp), title = {
                Text(text = "Routines")
            })
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

        }
    }
}