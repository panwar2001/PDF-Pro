package com.panwar2001.pdfpro.compose.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable

@Composable
fun SnackBarHost(snackBarHostState: SnackbarHostState){
    SnackbarHost(snackBarHostState){ data->
        val customVisuals= data.visuals as CustomSnackBarVisuals
         Snackbar(
                 snackbarData = data,
                 containerColor =  MaterialTheme.colorScheme.let { if(customVisuals.isError) it.errorContainer else it.onPrimaryContainer},
                 contentColor = when{
                     customVisuals.isError-> MaterialTheme.colorScheme.error
                     else ->SnackbarDefaults.contentColor
                 },
                 actionContentColor = when{
                     customVisuals.isError-> MaterialTheme.colorScheme.error
                     else ->SnackbarDefaults.contentColor
                 },
                 dismissActionContentColor = when{
                     customVisuals.isError-> MaterialTheme.colorScheme.error
                     else ->SnackbarDefaults.contentColor
                 }
            )
    }
}


data class CustomSnackBarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    val isError: Boolean=false
): SnackbarVisuals