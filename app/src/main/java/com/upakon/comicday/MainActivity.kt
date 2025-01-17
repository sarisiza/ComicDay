package com.upakon.comicday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upakon.comicday.ui.screens.ComicScreen
import com.upakon.comicday.ui.screens.ComicScreens
import com.upakon.comicday.ui.screens.StartScreen
import com.upakon.comicday.ui.theme.ComicDayTheme
import com.upakon.comicday.utils.ComicSource
import com.upakon.comicday.viewmodel.ComicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComicDayTheme {
                val viewModel = hiltViewModel<ComicViewModel>()
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text(text = "Comic of the Day") })
                    }
                ) { innerPadding ->
                    ComicNavGraph(
                        viewModel = viewModel,
                        paddingValues = innerPadding,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun ComicNavGraph(
    viewModel: ComicViewModel,
    paddingValues: PaddingValues,
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = ComicScreens.START.name
    ) {
        var source = ComicSource.Daily
        var currentId = 0
        composable(ComicScreens.START.name) {
            viewModel.getDailyComic()
            StartScreen(
                viewModel = viewModel,
                padding = paddingValues,
                onDaily = {
                    source = ComicSource.Daily
                    navController.navigate(ComicScreens.COMIC.name)
                },
                onSearch = {
                    source = ComicSource.Numbered
                    navController.navigate(ComicScreens.COMIC.name)
                    currentId = it
                }
            )
        }
        composable(ComicScreens.COMIC.name) {
            ComicScreen(
                viewModel = viewModel,
                source = source,
                paddingValues = paddingValues,
                onBack = {
                    navController.navigate(ComicScreens.START.name)
                },
                onErrorRetry = {
                    if (source == ComicSource.Daily){
                        viewModel.getDailyComic()
                    } else {
                        viewModel.getNumberedComic(currentId)
                    }
                }
            )
        }
    }
}