package com.upakon.comicday.data.service

import com.upakon.comicday.data.model.ComicModel
import com.upakon.comicday.data.service.DailyComicService.Companion.COMIC_ID
import com.upakon.comicday.data.service.DailyComicService.Companion.INFO_JSON
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DailyComicService {

    @GET(INFO_JSON)
    suspend fun getDailyComic(): Response<ComicModel>

    @GET(COMIC_ID + INFO_JSON)
    suspend fun getNumberedComic(
        @Path("id") id: Int
    ): Response<ComicModel>

    companion object{
        private const val INFO_JSON = "info.0.json"
        private const val COMIC_ID = "{id}/"
        const val BASE_URL = "https://xkcd.com/"
    }

}