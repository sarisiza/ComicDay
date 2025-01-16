package com.upakon.comicday.utils

import com.upakon.comicday.data.domain.DailyComic

sealed class UiState {

    object LOADING : UiState()

    data class SUCCESS(val result: DailyComic) : UiState()

    data class ERROR(val error: Exception) : UiState()

}