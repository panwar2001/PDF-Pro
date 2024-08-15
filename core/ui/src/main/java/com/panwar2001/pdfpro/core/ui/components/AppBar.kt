package com.panwar2001.pdfpro.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.ui.R
import kotlinx.coroutines.launch

data class MenuItem(
  val titleResId: Int,
  val onClick: ()->Unit
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navigationIcon:ImageVector=Icons.Default.Menu,
           menuItems:List<MenuItem> = listOf(),
           onNavigationIconClick: () -> Unit,
           titleComposable: @Composable ()->Unit={}){
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {titleComposable()},
        navigationIcon = {
            IconButton(onClick = {onNavigationIconClick() }) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = "Navigation Bar Icon",
                    modifier= Modifier.size(50.dp)
                )
            }
        },
        actions = {
            if (menuItems.isNotEmpty()) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "More options",
                        modifier = Modifier.size(50.dp),
                    )
                }
                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    menuItems.forEach {
                        DropdownMenuItem(text = {
                            Text(text = stringResource(id = it.titleResId))
                        },modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.spacing_large)),
                            onClick = {
                                it.onClick()
                                expanded = false
                            })
                    }
                }
            }
        })

}

@Preview
@Composable
fun AppBarPreview(drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)){
    val textToDisplay by remember {
        mutableStateOf("")
    }
    val scope= rememberCoroutineScope()
    val menu= listOf<MenuItem>()
    Column (
        Modifier
            .fillMaxSize())
    {
        Text(text = textToDisplay,color=Color.White)
        AppBar(onNavigationIconClick = {scope.launch{drawerState.open()}}, menuItems = menu , titleComposable = {})
    }
}


