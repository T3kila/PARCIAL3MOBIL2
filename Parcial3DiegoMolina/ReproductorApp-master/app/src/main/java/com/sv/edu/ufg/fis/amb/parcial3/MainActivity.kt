package com.sv.edu.ufg.fis.amb.parcial3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sv.edu.ufg.fis.amb.parcial3.ui.theme.ReproductorAppTheme

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReproductorAppTheme(
                dynamicColor = false
            ) {
                navController = rememberNavController()
                SetupNavGraph(navController)
            }
        }
    }
}
