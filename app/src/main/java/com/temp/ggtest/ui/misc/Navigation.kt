package com.temp.ggtest.ui.misc

import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.temp.ggtest.R

/**
 * Navigation class which encompasses all navigation destinations
 * Routes follow Base/{Args} format where Base represents a unique path
 * and args represent arguments for that route
 */
sealed class Navigation(val route: String, @StringRes val title : Int) {
    companion object {
        const val NAV_ID = "id"

        /**
         * utility method to get route object from route string
         */
        fun fromRoute(route: String?) =
            when (route?.substringBefore("/")) {
                Hit.route.substringBefore("/") -> Hit
                Home.route.substringBefore("/") -> Home
                else -> Home
            }
    }

    /**
     * list of navigation arguments
     */
    abstract fun getNavArguments(): List<NamedNavArgument>

    /**
     * Home destination where app starts from
     */
    object Home : Navigation("HOME", R.string.title_Home) {
        fun NavController.navigateToHome() {
            navigate("HOME")
        }

        override fun getNavArguments(): List<NamedNavArgument> = listOf()
    }

    /**
     * Hit destination that views one hit item
     */
    object Hit : Navigation("HIT/{$NAV_ID}", R.string.title_Hit) {
        fun NavController.navigateToHit(id: String) {
            navigate("HIT/$id")
        }

        override fun getNavArguments(): List<NamedNavArgument> =
            listOf(navArgument(NAV_ID) {
                type = NavType.StringType
            })
    }
}