package com.example.senior.pages

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.senior.data.Screens
import com.example.senior.ui.theme.LightPink
import com.example.senior.ui.theme.PinkRed
import com.example.senior.viewmodels.GraphViewModel
import com.example.senior.viewmodels.PostViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyBottomAppBar(
    map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> = mutableMapOf(),
    postViewModel: PostViewModel = viewModel(),
    graphViewModel: GraphViewModel = viewModel()
) {
    val navController = rememberNavController()
    val selected = remember { mutableStateOf(Icons.Default.List) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { postViewModel.motorStop() },
                containerColor = PinkRed,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Default.Close, contentDescription = "motor stop")
            }
        },
        floatingActionButtonPosition = FabPosition.Center, // Center the FAB
        content = {
            Box(Modifier.fillMaxSize()) {
                // Main content area of the Scaffold
                MyNavHost(
                    context = LocalContext.current,
                    navController = navController,
                    postViewModel = postViewModel,
                    graphViewModel = graphViewModel,
                    map = map
                )
                // Custom BottomAppBar with upside-down concave cutout
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height
                        val fabRadius = 40.dp.toPx() // Radius of the concave cutout
                        val cutoutWidth = fabRadius * 2

                        val path = Path().apply {
                            moveTo(0f, height)
                            lineTo((width - cutoutWidth) / 2, height)
                            arcTo(
                                rect = Rect(
                                    left = (width - cutoutWidth) / 2,
                                    top = height - fabRadius,
                                    right = (width + cutoutWidth) / 2,
                                    bottom = height + fabRadius
                                ),
                                startAngleDegrees = 0f,
                                sweepAngleDegrees = 180f,
                                forceMoveTo = false
                            )
                            lineTo(width, height)
                            lineTo(width, 0f)
                            lineTo(0f, 0f)
                            close()
                        }
                        drawPath(path = path, color = LightPink, style = Fill)
                    }

                    // Row to hold IconButtons
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 64.dp) // Leave space for the FAB cutout
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                selected.value = Icons.Default.List
                                navController.navigate(Screens.Graph.screen) {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.List,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.List) Color.White else Color.DarkGray
                            )
                        }

                        IconButton(
                            onClick = {
                                selected.value = Icons.Default.AccountBox
                                navController.navigate(Screens.Image.screen) {
                                    popUpTo(0)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.AccountBox,
                                contentDescription = null,
                                modifier = Modifier.size(26.dp),
                                tint = if (selected.value == Icons.Default.AccountBox) Color.White else Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    )
}

//// concave up
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MyBottomAppBar3(
//    postViewModel: PostViewModel = viewModel(),
//    graphViewModel: GraphViewModel = viewModel()
//) {
//    val navController = rememberNavController()
//    val selected = remember { mutableStateOf(Icons.Default.List) }
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { postViewModel.motorStop() },
//                containerColor = Color.Red,
//                shape = CircleShape,
//                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
//            ) {
//                Icon(Icons.Default.Close, contentDescription = "motor stop")
//            }
//        },
//        floatingActionButtonPosition = FabPosition.Center,
//        content = {
//            Box(Modifier.fillMaxSize()) {
//                MyNavHost(
//                    context = LocalContext.current,
//                    navController = navController,
//                    postViewModel = postViewModel,
//                    graphViewModel = graphViewModel
//                )
//                // Custom BottomAppBar with concave cutout
//                Box(
//                    Modifier
//                        .fillMaxWidth()
//                        .height(56.dp)
//                        .align(Alignment.BottomCenter)
//                ) {
//                    Canvas(modifier = Modifier.fillMaxSize()) {
//                        val width = size.width
//                        val height = size.height
//                        val fabRadius = 50.dp.toPx() // Radius of the concave cutout
//                        val cutoutWidth = fabRadius * 2
//
//                        val path = Path().apply {
//                            moveTo(0f, 0f)
//                            lineTo((width - cutoutWidth) / 2, 0f)
//                            arcTo(
//                                rect = Rect(
//                                    left = (width - cutoutWidth) / 2,
//                                    top = -fabRadius,
//                                    right = (width + cutoutWidth) / 2,
//                                    bottom = fabRadius
//                                ),
//                                startAngleDegrees = 180f,
//                                sweepAngleDegrees = 180f,
//                                forceMoveTo = false
//                            )
//                            lineTo(width, 0f)
//                            lineTo(width, height)
//                            lineTo(0f, height)
//                            close()
//                        }
//                        drawPath(path = path, color = Color.Gray, style = Fill)
//                    }
//
//
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 64.dp)
//                            .align(Alignment.Center),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        IconButton(
//                            onClick = {
//                                selected.value = Icons.Default.List
//                                navController.navigate(Screens.Graph.screen) {
//                                    popUpTo(0)
//                                }
//                            }
//                        ) {
//                            Icon(
//                                Icons.Default.List,
//                                contentDescription = null,
//                                modifier = Modifier.size(26.dp),
//                                tint = if (selected.value == Icons.Default.List) Color.White else Color.DarkGray
//                            )
//                        }
//
//                        IconButton(
//                            onClick = {
//                                selected.value = Icons.Default.AccountBox
//                                navController.navigate(Screens.Image.screen) {
//                                    popUpTo(0)
//                                }
//                            }
//                        ) {
//                            Icon(
//                                Icons.Default.AccountBox,
//                                contentDescription = null,
//                                modifier = Modifier.size(26.dp),
//                                tint = if (selected.value == Icons.Default.AccountBox) Color.White else Color.DarkGray
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    )
//}


//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MyBottomAppBar2(
//    postViewModel: PostViewModel = viewModel(),
//    graphViewModel: GraphViewModel = viewModel()
//) {
//    val navController = rememberNavController()
//    val selected = remember { mutableStateOf(Icons.Default.List) }
//
//    Scaffold(
//        bottomBar = {
//            Box(
//                Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//                    .background(PurpleGrey80)
//            ) {
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .align(Alignment.Center)
//                        .padding(horizontal = 56.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    IconButton(
//                        onClick = {
//                            selected.value = Icons.Default.List
//                            navController.navigate(Screens.Graph.screen) {
//                                popUpTo(0)
//                            }
//                        }
//                    ) {
//                        Icon(
//                            Icons.Default.List,
//                            contentDescription = null,
//                            modifier = Modifier.size(26.dp),
//                            tint = if (selected.value == Icons.Default.List) Color.White else Color.DarkGray
//                        )
//                    }
//
//                    IconButton(
//                        onClick = {
//                            selected.value = Icons.Default.AccountBox
//                            navController.navigate(Screens.Image.screen) {
//                                popUpTo(0)
//                            }
//                        }
//                    ) {
//                        Icon(
//                            Icons.Default.AccountBox,
//                            contentDescription = null,
//                            modifier = Modifier.size(26.dp),
//                            tint = if (selected.value == Icons.Default.AccountBox) Color.White else Color.DarkGray
//                        )
//                    }
//                }
//            }
//        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { postViewModel.motorStop() },
//                containerColor = Color.Red,
//                shape = CircleShape,
//                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
//            ) {
//                Icon(Icons.Default.Close, contentDescription = "motor stop")
//            }
//        },
////        isFloatingActionButtonDocked = true, // Dock FAB into the bottom bar
//        floatingActionButtonPosition = FabPosition.Center // Position FAB in the center
//    ) {
//        MyNavHost(context = LocalContext.current, navController, postViewModel, graphViewModel)
//    }
//}
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MyBottomAppBar1(postViewModel: PostViewModel = viewModel(), graphViewModel: GraphViewModel = viewModel()) {
//    val navController = rememberNavController()
//    val context = LocalContext.current.applicationContext
//    val selected = remember {
//        mutableStateOf(Icons.Default.List)
//    }
//
//    Scaffold(
//        bottomBar = {
//            BottomAppBar(
//                containerColor = PurpleGrey80
//            ) {
//                IconButton(onClick = {
//                    selected.value = Icons.Default.List
//                    navController.navigate(Screens.Graph.screen){
//                        popUpTo(0)
//                    }
//                },
//                    modifier = Modifier.weight(1f)) {
//                    Icon(
//                        Icons.Default.List,
//                        contentDescription = null,
//                        modifier = Modifier.size(26.dp),
//                        tint = if (selected.value == Icons.Default.List) Color.White else Color.DarkGray
//                    )
//                }
//
//                FloatingActionButton(
//                    modifier = Modifier.align(Alignment.Top),
//                    onClick = { postViewModel.motorStop() },
//                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
//                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
//                ) {
//                    Icon(Icons.Default.Close, contentDescription = "motor stop")
//                }
//
//
//                IconButton(onClick = {
//                    selected.value = Icons.Default.AccountBox
//                    navController.navigate(Screens.Image.screen){
//                        popUpTo(0)
//                    }
//                },
//                    modifier = Modifier.weight(1f)) {
//                    Icon(
//                        Icons.Default.AccountBox,
//                        contentDescription = null,
//                        modifier = Modifier.size(26.dp),
//                        tint = if (selected.value == Icons.Default.AccountBox) Color.White else Color.DarkGray
//                    )
//                }
//
//
//            }
//        }
//    ) {
//        MyNavHost(context, navController, postViewModel, graphViewModel)
//    }
//}


@Composable
fun MyNavHost(context: Context, navController: NavHostController, postViewModel: PostViewModel, graphViewModel: GraphViewModel, map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>> = mutableMapOf()) {
    NavHost(navController = navController, startDestination = Screens.Graph.screen) {
        composable(Screens.Graph.screen) {
            GraphPage(map)
        }
        composable(Screens.Image.screen) {
            GridView(context, postViewModel, graphViewModel, map)
        }
    }
}
