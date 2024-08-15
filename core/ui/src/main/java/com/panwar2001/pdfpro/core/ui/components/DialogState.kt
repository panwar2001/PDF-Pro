package com.panwar2001.pdfpro.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.panwar2001.pdfpro.core.ui.R

/**
 * Create and [remember] a [PasswordDialogState].
 *
 * @param initialValue the initial showing state of the dialog
 */
class PasswordDialogState(initialValue: Boolean = false,isPasswordError:Boolean) {
    var showing by mutableStateOf(initialValue)
    var isPasswordError by mutableStateOf(isPasswordError)

    /**
     *  Shows the dialog
     */
    fun show() {
        showing = true
    }

    fun wrongPassword(){
        isPasswordError=true
    }

    /**
     * Clears focus with a given [FocusManager] and then hides the dialog
     *
     * @param focusManager the focus manager of the dialog view
     */
    fun hide(focusManager: FocusManager? = null) {
        focusManager?.clearFocus()
        showing = false
        isPasswordError=false
    }

    companion object {
        /**
         * The default [Saver] implementation for [DialogState].
         */
        fun Saver(): Saver<PasswordDialogState, *> = Saver(
            save = { listOf(it.showing, it.isPasswordError) }, // Save both states as a list
            restore = {
                val (showing, isPasswordError) = it
                PasswordDialogState(showing, isPasswordError)
            }
        )
    }
}

/**
 * Create and [remember] a [DialogState].
 *
 * @param initialValue the initial showing state of the dialog
 */
@Composable
fun rememberDialogState(initialValue: Boolean = false): PasswordDialogState {
    return rememberSaveable(saver = PasswordDialogState.Saver()) {
        PasswordDialogState(initialValue,initialValue)
    }
}

@Composable
fun passwordInputDialogState(onConfirm: (String) -> Unit) =
    rememberDialogState().also { dialogState->
      if(dialogState.showing){
       PasswordInputDialog(onConfirm = onConfirm,
       onDismiss={dialogState.hide()},
       isPasswordError =dialogState.isPasswordError)
   }
}

@Composable
fun PasswordInputDialog(
    onConfirm: (String)->Unit,
    onDismiss:()->Unit,
    isPasswordError: Boolean
){
    var password by remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {onConfirm(password)},
                enabled = password.trim().isNotEmpty()) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.close))
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    isError = isPasswordError,
                    label = { Text(stringResource(id = R.string.pdf_key_input)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
                if(isPasswordError){
                    Text(text = stringResource(id = R.string.decrypt_password_wrong),
                        color = Color.Red)
                }
            }
        }
    )
}