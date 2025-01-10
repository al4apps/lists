package com.al4apps.mycomposeapp.vialackner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.al4apps.mycomposeapp.vialackner.Screen.Companion.NAME_ARG_KEY

@Composable
fun NavigationL() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreenL(navController)
        }
        composable(
            route = Screen.DetailScreen.route + "/{${Screen.NAME_ARG_KEY}}",
            arguments = listOf(
                navArgument(NAME_ARG_KEY) {
                    type = NavType.StringType
                    defaultValue = "NoName"
                    nullable = true
                }
            )
        ) { entry ->
            val name = entry.arguments?.getString(NAME_ARG_KEY)
            DetailScreen(name)
        }
    }
}

@Composable
fun DetailScreen(name: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Hello, $name")
    }

}

@Composable
fun HomeScreenL(navController: NavHostController) {
    var text by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(value = text, modifier = Modifier.fillMaxWidth(), onValueChange = {
            text = it
        })
        Button(onClick = {
            navController.navigate(Screen.DetailScreen.withArgs(text))
        }) {
            Text("to Detail")
        }
    }
}
