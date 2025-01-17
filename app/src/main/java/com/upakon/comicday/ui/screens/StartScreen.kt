package com.upakon.comicday.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.upakon.comicday.R
import com.upakon.comicday.data.domain.DailyComic
import com.upakon.comicday.utils.UiState
import com.upakon.comicday.viewmodel.ComicViewModel
import kotlin.random.Random

private const val TAG = "StartScreen"
@Composable
fun StartScreen(
    viewModel: ComicViewModel,
    padding: PaddingValues,
    onDaily: () -> Unit,
    onSearch: (Int) -> Unit
){
    var showError by remember {
        mutableStateOf(false)
    }
    var errorMessage by remember {
        mutableStateOf("")
    }
    var confirmAction: (() -> Unit)? = null
    when (val state = viewModel.dailyComic.collectAsState(UiState.LOADING).value){
        is UiState.ERROR -> {
            Log.d(TAG, "StartScreen: Error")
            showError = true
            errorMessage = state.error.localizedMessage ?: "Network error"
            confirmAction = {
                viewModel.getDailyComic()
            }
        }
        UiState.LOADING -> {
            Log.d(TAG, "StartScreen: Loading")
            CircularProgressIndicator()
        }
        is UiState.SUCCESS -> {
            val dailyComic = state.result
            Log.d(TAG, "Success: ${dailyComic.num}")
            Column(
                modifier = Modifier.padding(padding)
            ) {
                var searchText by remember {
                    mutableStateOf("")
                }
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {searchText = it},
                    label = { Text(text = "Search")},
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        keyboardType = KeyboardType.Number
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (searchText.isNotEmpty()){
                                    try {
                                        val id = searchText.toInt()
                                        if (id <= dailyComic.num){
                                            viewModel.getNumberedComic(id)
                                            onSearch(id)
                                        } else throw Exception("We don't have that comic yet")
                                    } catch (e: Exception){
                                        showError = true
                                        errorMessage = e.localizedMessage ?: "Search error"
                                        confirmAction = {showError = false}
                                    }
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_search_24),
                                contentDescription = "Search")
                        }
                    }
                )
                Button(
                    onClick = { onDaily() }
                ) {
                    Text(text = "${dailyComic.month}/${dailyComic.year}")
                }
                Button(
                    onClick = {
                        val randId = Random.nextInt(dailyComic.num)
                        viewModel.getNumberedComic(randId)
                        onSearch(randId)
                    }
                ) {
                    Text(text = "Random comic")
                }
            }
        }
    }
    if(showError){
        ErrorDialog(
            message = errorMessage,
            onDismiss = { showError = false },
            onRetry = confirmAction
        )
    }
}