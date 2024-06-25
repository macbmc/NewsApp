package com.loc.newsapp.presentation.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph(mViewModel: HomeViewModel,navController: NavHostController) {

    NavHost(navController = navController, startDestination = "Home" )
    {
        composable("Home")
        {
            HomeScreen(mViewModel = mViewModel)
        }
        composable("BookMarks")
        {
            BookMarksScreen()
        }

    }

}