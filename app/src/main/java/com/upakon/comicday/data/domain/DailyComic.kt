package com.upakon.comicday.data.domain

import com.upakon.comicday.data.model.ComicModel

data class DailyComic(
    val num: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val title: String,
    val img: String?,
    val alt: String?
)

fun ComicModel.toDailyComic(): DailyComic =
    DailyComic(
        this.num ?: 1,
        this.day?.toInt() ?: 1,
        this.month?.toInt() ?: 1,
        this.year?.toInt() ?: 2025,
        this.title ?: "Unknown title",
        this.img,
        this.alt
    )
