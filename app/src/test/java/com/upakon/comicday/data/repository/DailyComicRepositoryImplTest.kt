package com.upakon.comicday.data.repository

import com.upakon.comicday.data.model.ComicModel
import com.upakon.comicday.data.service.DailyComicService
import com.upakon.comicday.utils.UiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DailyComicRepositoryImplTest {

    private lateinit var repository: DailyComicRepository


    private val mockService = mockk<DailyComicService>()
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        repository = DailyComicRepositoryImpl(mockService)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test getDailyComic when response is successful and is not empty`() = runTest(dispatcher) {

        coEvery { mockService.getDailyComic() } returns mockk{
            every { isSuccessful } returns true
            every { body() } returns mockk{
                every { num } returns 3
                every { day } returns "15"
                every { month } returns "1"
                every { year } returns "2025"
                every { title } returns "Test title"
                every { img } returns "Test img"
                every { alt } returns "Test alt"
            }
        }

        val states = repository.getDailyComic().toList()

        assert(states.size == 2)
        assert(states.first() is UiState.LOADING)
        assert(states.last() is UiState.SUCCESS)
        assert((states.last() as UiState.SUCCESS).result.title == "Test title")

    }

    @Test
    fun `test getDailyComic when response is successful and is empty`() = runTest(dispatcher) {

        coEvery { mockService.getDailyComic() } returns mockk{
            every { isSuccessful } returns true
            every { body() } returns null
        }

        val states = repository.getDailyComic().toList()

        assert(states.size == 2)
        assert(states.first() is UiState.LOADING)
        assert(states.last() is UiState.ERROR)
        assert((states.last() as UiState.ERROR).error.localizedMessage == "Response not found")

    }

    @Test
    fun `test getDailyComic when response is not successful`() = runTest(dispatcher) {

        coEvery { mockService.getDailyComic() } returns mockk{
            every { isSuccessful } returns false
            every { errorBody() } returns mockk{
                every { string() } returns "Test error"
            }
        }

        val states = repository.getDailyComic().toList()

        assert(states.size == 2)
        assert(states.first() is UiState.LOADING)
        assert(states.last() is UiState.ERROR)
        assert((states.last() as UiState.ERROR).error.localizedMessage == "Test error")

    }

    @Test
    fun `test getNumberedComic when response is successful and is not empty`() = runTest(dispatcher) {

        coEvery { mockService.getNumberedComic(any()) } returns mockk{
            every { isSuccessful } returns true
            every { body() } returns mockk{
                every { num } returns 3
                every { day } returns "15"
                every { month } returns "1"
                every { year } returns "2025"
                every { title } returns "Test title"
                every { img } returns "Test img"
                every { alt } returns "Test alt"
            }
        }

        val states = repository.getNumberedComic(3).toList()

        assert(states.size == 2)
        assert(states.first() is UiState.LOADING)
        assert(states.last() is UiState.SUCCESS)
        assert((states.last() as UiState.SUCCESS).result.title == "Test title")

    }

    @Test
    fun `test getNumberedComic when response is successful and is empty`() = runTest(dispatcher) {

        coEvery { mockService.getNumberedComic(any()) } returns mockk{
            every { isSuccessful } returns true
            every { body() } returns null
        }

        val states = repository.getNumberedComic(3).toList()

        assert(states.size == 2)
        assert(states.first() is UiState.LOADING)
        assert(states.last() is UiState.ERROR)
        assert((states.last() as UiState.ERROR).error.localizedMessage == "Response not found")

    }

    @Test
    fun `test getNumberedComic when response is not successful`() = runTest(dispatcher) {

        coEvery { mockService.getNumberedComic(any()) } returns mockk{
            every { isSuccessful } returns false
            every { errorBody() } returns mockk{
                every { string() } returns "Test error"
            }
        }

        val states = repository.getNumberedComic(3).toList()

        assert(states.size == 2)
        assert(states.first() is UiState.LOADING)
        assert(states.last() is UiState.ERROR)
        assert((states.last() as UiState.ERROR).error.localizedMessage == "Test error")

    }


}