package com.upakon.comicday.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.upakon.comicday.data.domain.DailyComic
import com.upakon.comicday.data.repository.DailyComicRepository
import com.upakon.comicday.utils.UiState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ComicViewModelTest {

    @get:Rule val instantTask = InstantTaskExecutorRule()
    private lateinit var viewModel: ComicViewModel

    private val mockRepository = mockk<DailyComicRepository>()
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = ComicViewModel(mockRepository,dispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test getDailyComic`() = runTest(dispatcher){
        every { mockRepository.getDailyComic() } returns flowOf(
            UiState.SUCCESS(
                DailyComic(
                    1,
                    1,
                    1,
                    2025,
                    "Test comic",
                    "Test img",
                    null
                )
            )
        )
        viewModel.getDailyComic()
        val state = viewModel.dailyComic.first()

        assert(state is UiState.SUCCESS)
        assert((state as UiState.SUCCESS).result.title == "Test comic")
    }

    @Test
    fun `test getNumberedComic`() = runTest(dispatcher){
        every { mockRepository.getNumberedComic(any()) } returns flowOf(
            UiState.SUCCESS(
                DailyComic(
                    1,
                    1,
                    1,
                    2025,
                    "Test comic",
                    "Test img",
                    null
                )
            )
        )
        viewModel.getNumberedComic(1)
        val state = viewModel.currentComic.first()

        assert(state is UiState.SUCCESS)
        assert((state as UiState.SUCCESS).result.title == "Test comic")
    }

}