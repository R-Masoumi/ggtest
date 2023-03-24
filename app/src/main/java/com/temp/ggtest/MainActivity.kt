package com.temp.ggtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.temp.ggtest.ui.component.HitComponent
import com.temp.ggtest.ui.component.HomeComponent
import com.temp.ggtest.ui.misc.Navigation
import com.temp.ggtest.ui.misc.Navigation.Home.navigateToHome
import com.temp.ggtest.ui.misc.Navigator
import com.temp.ggtest.ui.misc.iconId
import com.temp.ggtest.ui.theme.GgtestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

/**
 * MainActivity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var navigator : Navigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GgtestTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    navigator.initialize( navController)
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AppBar(navController)
                        MainScreen(navController)
                    }
                }
            }
        }
    }
}

/**
 * AppBar for the top of the application
 * @param navController used to infer where we are in the app and change AppBar accordingly
 */
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AppBar(navController: NavHostController) {
    val currentRoute = navController
        .currentBackStackEntryFlow
        .collectAsStateWithLifecycle(initialValue = navController.currentBackStackEntry)

    val currentNav = Navigation.fromRoute(currentRoute.value?.destination?.route)
    val showBackButton = when (currentNav) {
        Navigation.Home -> false
        else -> true
    }
    TopAppBar(
        title = {
            Text(stringResource(id = currentNav.title))
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.semantics { iconId = "back" }
                ) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.desc_img_back)) }
            } else {
                IconButton(
                    modifier = Modifier.semantics { iconId = "home" },
                    onClick = { navController.popBackStack(Navigation.Home.route, false) }
                ) { Icon(imageVector = Icons.Default.Home, contentDescription = stringResource(R.string.desc_img_home)) }
            }
        }
    )
}

/**
 * MainScreen that leads to the correct page using navigation
 * @param navController used to compose the correct page
 */
@Composable
fun MainScreen(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Navigation.Home.route
    ) {
        composable(Navigation.Home.route, Navigation.Home.getNavArguments()) {
            HomeComponent()
        }
        composable(Navigation.Hit.route, Navigation.Hit.getNavArguments()) {
            HitComponent()
        }
    }
}