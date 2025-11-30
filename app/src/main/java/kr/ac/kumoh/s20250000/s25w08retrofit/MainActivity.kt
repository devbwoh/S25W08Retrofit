package kr.ac.kumoh.s20250000.s25w08retrofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import kr.ac.kumoh.s20250000.s25w08retrofit.view.SongAddDialog
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

    val showingAddDialog = remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerSheet(scope, drawerState, navController)
        },
        gesturesEnabled = true,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(scope, drawerState)
            },
            bottomBar = {
                BottomBar(navController) {
                    navigateAndClearStack(navController, it)
                }
            },
            floatingActionButton = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 노래 화면일 때만 FAB 표시
                if (currentRoute == SONG_SCREEN) {
                    FloatingActionButton(
                        onClick = {
                            showingAddDialog.value = true
                            //viewModel.deleteSong("115cff3c-3bdb-4938-95a6-97f5c3a949c8")
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "노래 추가")
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = SONG_SCREEN,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(SONG_SCREEN) {
                    SongList(
                        songList,
                        onDelete = { songId ->
                            viewModel.deleteSong(songId)
                        }
                    ) {
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

    if (showingAddDialog.value) {
        SongAddDialog(viewModel) {
            showingAddDialog.value = false
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    CenterAlignedTopAppBar(
        title = { Text("나의 취미") },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "메뉴 아이콘"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Composable
fun BottomBar(
    navController: NavHostController,
    onNavigate: (String) -> Unit
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar {
        NavigationBarItem(
            label = {
                Text("노래")
            },
            icon = {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "노래 아이콘"
                )
            },
            selected = currentDestination?.route == SONG_SCREEN,
            onClick = {
                onNavigate(SONG_SCREEN)
            }
        )
        NavigationBarItem(
            label = {
                Text("가수")
            },
            icon = {
                Icon(
                    Icons.Default.Face,
                    contentDescription = "가수 아이콘"
                )
            },
            selected = currentDestination?.route == SINGER_SCREEN,
            onClick = {
                onNavigate(SINGER_SCREEN)
            }
        )
    }
}