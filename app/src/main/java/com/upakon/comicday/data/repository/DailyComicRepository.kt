package com.upakon.comicday.data.repository

import com.upakon.comicday.data.domain.DailyComic
import com.upakon.comicday.utils.UiState
import kotlinx.coroutines.flow.Flow

interface DailyComicRepository {

    fun getDailyComic(): Flow<UiState>

    fun getNumberedComic(id: Int): Flow<UiState>

}