package com.al4apps.lists.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.al4apps.lists.domain.Constants.NEW_LIST_ID
import com.al4apps.lists.presentation.home.HomeScreen
import com.al4apps.lists.presentation.fund.FundScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = AppScreens.Home.route) {
        composable(AppScreens.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = AppScreens.List.route + "/{${AppScreens.LIST_ID_ARG_KEY}}",
            arguments = listOf(
                navArgument(
                    name = AppScreens.LIST_ID_ARG_KEY, builder = {
                        type = NavType.IntType
                        defaultValue = NEW_LIST_ID
                        nullable = false
                    })
            )
        ) { entry ->
            val listId = entry.arguments?.getInt(AppScreens.LIST_ID_ARG_KEY) ?: NEW_LIST_ID

            FundScreen(navController, listId)
        }
    }
}

sealed class AppScreens(val route: String) {
    data object Home : AppScreens("home_screen")
    data object List : AppScreens("list_screen")

    fun withArgs(vararg args: Int): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    companion object {
        const val LIST_ID_ARG_KEY = "listId"
    }
}

