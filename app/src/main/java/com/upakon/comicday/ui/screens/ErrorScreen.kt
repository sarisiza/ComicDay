package com.upakon.comicday.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            if (onRetry != null){
                Button(onClick = { onRetry() }) {
                    Text(text = "Retry")
                }
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "Dismiss")
            }
        },
        text = {
            Text(text = message)
        }
    )
}