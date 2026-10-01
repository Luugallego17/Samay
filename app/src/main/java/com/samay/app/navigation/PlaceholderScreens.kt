package com.samay.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PlaceholderScreen(
    title: String,
    owner: String,
    onNext: (() -> Unit)? = null,
    nextLabel: String = "Next",
    onSecondary: (() -> Unit)? = null,
    secondaryLabel: String = ""
) {
    Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = "TODO $owner",
            style = MaterialTheme.typography.bodyMedium,
            modifier = androidx.compose.ui.Modifier.padding(top = 8.dp)
        )
        if (onNext != null) {
            Button(
                onClick = onNext,
                modifier = androidx.compose.ui.Modifier.padding(top = 24.dp)
            ) {
                Text(nextLabel)
            }
        }
        if (onSecondary != null) {
            TextButton(onClick = onSecondary) {
                Text(secondaryLabel)
            }
        }
    }
}
