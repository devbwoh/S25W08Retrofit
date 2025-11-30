package kr.ac.kumoh.s20250000.s25w08retrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kr.ac.kumoh.s20250000.s25w08retrofit.navigation.Screens.SINGER_SCREEN
import kr.ac.kumoh.s20250000.s25w08retrofit.navigation.Screens.SONG_DETAIL_ROUTE
import kr.ac.kumoh.s20250000.s25w08retrofit.navigation.Screens.SONG_DETAIL_SCREEN
import kr.ac.kumoh.s20250000.s25w08retrofit.navigation.Screens.SONG_ID_ARG
import kr.ac.kumoh.s20250000.s25w08retrofit.navigation.Screens.SONG_SCREEN
import kr.ac.kumoh.s20250000.s25w08retrofit.ui.theme.S25W08RetrofitTheme
import kr.ac.kumoh.s20250000.s25w08retrofit.view.SingerScreen
import kr.ac.kumoh.s20250000.s25w08retrofit.view.SongDetailScreen
import kr.ac.kumoh.s20250000.s25w08retrofit.view.SongList
import kr.ac.kumoh.s20250000.s25w08retrofit.viewmodel.SongViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S25W08RetrofitTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: SongViewModel = viewModel()) {
    val songList by viewModel.songList.collectAsState()

    val navController = rememberNavController()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerSheet(scope, drawerState, navController)
        },
        gesturesEnabled = true,
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = SONG_SCREEN,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(SONG_SCREEN) {
                    SongList(songList) {
                        // Stack은 유지하면서 중복 생성만 방지함
                        navController.navigate("$SONG_DETAIL_SCREEN/$it") {
                            launchSingleTop = true
                        }
                    }
                }

                composable(
                    route = SONG_DETAIL_ROUTE,
                    arguments = listOf(
                        navArgument(SONG_ID_ARG) {
                            type = NavType.StringType
                        },
                    )
                ) {
                    SongDetailScreen(
                        it.arguments?.getString(SONG_ID_ARG),
                        viewModel = viewModel
                    )
                }

                composable(SINGER_SCREEN) {
                    SingerScreen()
                }
            }
        }
    }
}

private fun navigateAndClearStack(
    navController: NavHostController,
    route: String
) {
    navController.navigate(route) {
        launchSingleTop = true

        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}

@Composable
fun DrawerSheet(
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text("노래 화면") },
            selected = currentDestination?.route == SONG_SCREEN,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                navigateAndClearStack(navController, SONG_SCREEN)
            },
            icon = {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "노래 아이콘"
                )
            }
        )
        NavigationDrawerItem(
            label = { Text("가수 화면") },
            selected = currentDestination?.route == SINGER_SCREEN,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                navigateAndClearStack(navController, SINGER_SCREEN)
            },
            icon = {
                Icon(
                    Icons.Default.Face,
                    contentDescription = "가수 아이콘"
                )
            }
        )
    }
}
