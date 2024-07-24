package com.panwar2001.pdfpro.compose.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

@Composable
fun SnackBarHost(snackBarHostState: SnackbarHostState,isError: Boolean){
    SnackbarHost(snackBarHostState){ data->
        Snackbar(
            snackbarData = data,
                 containerColor =  MaterialTheme.colorScheme.let { if(isError) it.errorContainer else it.onPrimaryContainer},
                 contentColor = when{
                     isError-> MaterialTheme.colorScheme.error
                     else ->SnackbarDefaults.contentColor
                 },
                 actionContentColor = when{
                     isError-> MaterialTheme.colorScheme.error
                     else ->SnackbarDefaults.contentColor
                 },
            )
    }
}

