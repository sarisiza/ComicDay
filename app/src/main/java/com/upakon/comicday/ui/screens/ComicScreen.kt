package com.upakon.comicday.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.upakon.comicday.R
import com.upakon.comicday.utils.ComicSource
import com.upakon.comicday.utils.UiState
import com.upakon.comicday.viewmodel.ComicViewModel

@Composable
fun ComicScreen(
    viewModel: ComicViewModel,
    source: ComicSource,
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onErrorRetry: (() -> Unit)
){
    val state = if (source == ComicSource.Daily)
        viewModel.dailyComic.collectAsState().value
    else
        viewModel.currentComic.collectAsState().value
    var showError by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }
    when (state){
        is UiState.ERROR -> {
            showError = true
            errorMessage = state.error.localizedMessage ?: "Network error"
        }
        UiState.LOADING -> {
            CircularProgressIndicator()
        }
        is UiState.SUCCESS -> {
            val comic = state.result
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Text(
                    text = comic.title,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${comic.month}/${comic.day}/${comic.year}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(comic.img)
                        .error(R.drawable.baseline_broken_image_24)
                        .build(),
                    contentDescription = comic.alt
                )
                Button(onClick = { onBack() }) {
                    Text(text = "Back")
                }
            }
        }
    }
    if(showError){
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showError = false },
            onRetry = onErrorRetry
        )
    }
}