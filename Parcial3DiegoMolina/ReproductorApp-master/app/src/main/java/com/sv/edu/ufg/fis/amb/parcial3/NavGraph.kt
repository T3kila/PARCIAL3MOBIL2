package com.sv.edu.ufg.fis.amb.parcial3

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sv.edu.ufg.fis.amb.parcial3.pages.MainPage
import com.sv.edu.ufg.fis.amb.parcial3.pages.PhotoCapturePage
import com.sv.edu.ufg.fis.amb.parcial3.pages.PhotoGalleryPage
import com.sv.edu.ufg.fis.amb.parcial3.pages.VideoListPage
import com.sv.edu.ufg.fis.amb.parcial3.pages.VideoPlayerPage
import com.sv.edu.ufg.fis.amb.parcial3.pages.VideoRecorderPage
import com.sv.edu.ufg.fis.amb.parcial3.routes.ROOT_PLAYER_PAGE
import com.sv.edu.ufg.fis.amb.parcial3.routes.Routes
import com.sv.edu.ufg.fis.amb.parcial3.viewModels.MainViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.mainpage.route
    ){
        composable(
            route = Routes.mainpage.route
        ){
            MainPage(
                onRecordVideo = { navController.navigate(Routes.recordpage.route) },
                onViewVideos = { navController.navigate(Routes.videospage.route) },
                onPlayLastVideo = {
                    val lastVideo = viewModel.getLastVideo()
                    if (lastVideo != null) {
                        navController.navigate("${ROOT_PLAYER_PAGE}?uri=${lastVideo.uri}")
                    }
                },
                onOpenCamera = { navController.navigate(Routes.photopage.route) }, // Agrega esta línea para manejar onOpenCamera
                onViewPhotos = { navController.navigate(Routes.gallerypage.route) }
            )
        }
        composable(
            route = Routes.recordpage.route
        ) {
            VideoRecorderPage(
                onVideoRecorded = { uri ->
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.videospage.route
        ) {
            VideoListPage(
                onVideoSelected = { videoFile ->
                    navController.navigate("${ROOT_PLAYER_PAGE}?uri=${videoFile.uri}")
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Routes.playerpage.route,
            arguments = listOf(navArgument("uri") { type = NavType.StringType })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri")
            if (uriString != null) {
                VideoPlayerPage(videoUri = Uri.parse(uriString))
            }
        }

        composable(
            route = Routes.photopage.route
        ) {
            PhotoCapturePage(
                onPhotoCaptured = { uri ->
                    navController.popBackStack() // Regresa a la pantalla principal después de capturar la foto
                }
            )
        }
        composable(route = Routes.gallerypage.route) {
            val context = LocalContext.current
            PhotoGalleryPage(context = context)
        }
    }
}
