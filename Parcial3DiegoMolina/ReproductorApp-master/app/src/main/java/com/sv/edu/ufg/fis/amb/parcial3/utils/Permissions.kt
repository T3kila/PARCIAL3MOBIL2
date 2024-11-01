package com.sv.edu.ufg.fis.amb.parcial3.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(
    permissions: List<String>,
    onPermissionsResult: (Boolean) -> Unit
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)
    LaunchedEffect(Unit) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    onPermissionsResult(multiplePermissionsState.allPermissionsGranted)
}