package com.upakon.comicday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upakon.comicday.data.repository.DailyComicRepository
import com.upakon.comicday.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ComicViewModel"
@HiltViewModel
class ComicViewModel @Inject constructor(
    private val repository: DailyComicRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _dailyComic: MutableStateFlow<UiState> = MutableStateFlow(UiState.LOADING)
    val dailyComic: StateFlow<UiState> get() = _dailyComic

    private val _currentComic: MutableStateFlow<UiState> = MutableStateFlow(UiState.LOADING)
    val currentComic: StateFlow<UiState> get() = _currentComic

    fun getDailyComic(){
        viewModelScope.launch(dispatcher) {
            repository.getDailyComic().collect{
                _dailyComic.value = it
            }
        }
    }

    fun getNumberedComic(id: Int){
        viewModelScope.launch(dispatcher) {
            repository.getNumberedComic(id).collect{
                _currentComic.value = it
            }
        }
    }

}