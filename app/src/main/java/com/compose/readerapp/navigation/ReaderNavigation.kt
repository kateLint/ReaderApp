package com.compose.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.readerapp.screens.ReaderSplashScreen
import com.compose.readerapp.screens.details.BookDetailsScreen
import com.compose.readerapp.screens.home.ReaderHomeScreen
import com.compose.readerapp.screens.login.ReaderLoginScreen
import com.compose.readerapp.screens.search.BooksSearchViewModel
import com.compose.readerapp.screens.search.ReaderBookSearchScreen
import com.compose.readerapp.screens.stats.ReaderStatScreen
import com.compose.readerapp.screens.update.ReaderBookUpdateScreen

@Composable
fun ReaderNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name){
        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }

        composable(ReaderScreens.ReaderStatsScreen.name) {
        //    val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatScreen(navController= navController)
        }

        composable(ReaderScreens.ReaderHomeScreen.name) {
        //    val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderHomeScreen(navController = navController)
        }

        composable(ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<BooksSearchViewModel>()
            ReaderBookSearchScreen(navController = navController, searchViewModel)
        }

        composable(ReaderScreens.UpdateScreen.name) {
            //  val searchViewModel = hiltViewModel<BooksSearchViewModel>()

            ReaderBookUpdateScreen(navController = navController)
        }

        val detailsName = ReaderScreens.DetailScreen.name
        composable("$detailsName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })) {backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
            //    val homeViewModel = hiltViewModel<HomeScreenViewModel>()

        }
    }
}