package com.panwar2001.pdfpro.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String,
    modifier: Modifier,
    onNavigationIconClick: () -> Unit
) {
    TextField(
        value = state.value,
        onValueChange = {value->
            state.value = value
        },
        modifier
            .fillMaxWidth()
            .background(color = Color.Red.copy(alpha = .0f))
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .border(border = BorderStroke(0.dp, Color.Transparent),shape= RoundedCornerShape(40.dp)),
        shape = RoundedCornerShape(40.dp),
        placeholder = {
            Text(text = placeHolder)
        },
        leadingIcon = {
            IconButton(onClick = {onNavigationIconClick() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Navigation Menu"
                )
            }
        },
        trailingIcon = {
            Icon(modifier=modifier.clickable {
                if(state.value.text.isNotEmpty()){
                    state.value= TextFieldValue("")
                }
            },
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear")
        },
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 20.sp
        )
    )
}