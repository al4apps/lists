package com.al4apps.lists.vialackner

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home")
    data object DetailScreen : Screen("detail")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    companion object {
        const val NAME_ARG_KEY = "name"
    }
}