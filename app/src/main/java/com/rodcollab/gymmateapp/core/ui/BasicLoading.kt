package com.rodcollab.gymmateapp.core.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicLoading(title: String) {
    WidgetDialog {
        Text(text = title)
        Spacer(modifier = Modifier.size(16.dp))
        CircularProgressIndicator(strokeWidth = 2.dp)
    }
}