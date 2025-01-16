package com.upakon.comicday.data.repository

import com.upakon.comicday.data.domain.toDailyComic
import com.upakon.comicday.data.service.DailyComicService
import com.upakon.comicday.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DailyComicRepositoryImpl(
    private val service: DailyComicService
) : DailyComicRepository {

    override fun getDailyComic(): Flow<UiState> = flow {
        emit(UiState.LOADING)
        try {
            val response = service.getDailyComic()
            if (response.isSuccessful){
                response.body()?.let {
                    emit(UiState.SUCCESS(it.toDailyComic()))
                } ?: throw Exception("Response not found")
            } else throw Exception(response.errorBody()?.toString())
        } catch (e: Exception){
            emit(UiState.ERROR(e))
        }
    }

    override fun getNumberedComic(id: Int): Flow<UiState> = flow{
        emit(UiState.LOADING)
        try {
            val response = service.getNumberedComic(id)
            if (response.isSuccessful){
                response.body()?.let {
                    emit(UiState.SUCCESS(it.toDailyComic()))
                } ?: throw Exception("Response not found")
            } else throw Exception(response.errorBody()?.toString())
        } catch (e: Exception){
            emit(UiState.ERROR(e))
        }
    }


}