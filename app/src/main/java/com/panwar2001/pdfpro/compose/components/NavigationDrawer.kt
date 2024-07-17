package com.panwar2001.pdfpro.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.compose.CircularIcon
import com.panwar2001.pdfpro.navigation.Screens
import com.panwar2001.pdfpro.data.DataSource
import com.panwar2001.pdfpro.data.ToolsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AppModalNavigationDrawer(
    drawerState: DrawerState,
    setTheme: (Boolean) -> Unit,
    currentTheme: Boolean,
    scope: CoroutineScope= rememberCoroutineScope(),
    navigateTo: (String) -> Unit,
    content: @Composable ()->Unit
){
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(Modifier.padding(0.dp,0.dp,60.dp,0.dp)) {
                DrawerHeader()
                DrawerBody(items = DataSource.ToolsList,
                    setTheme=setTheme,
                    currentTheme=currentTheme){
                    if(drawerState.isOpen){
                        scope.launch {drawerState.apply {close()}}
                    }
                    navigateTo(it)
                }
            }
        },
        gesturesEnabled = drawerState.isOpen)
    {
       content()
    }

}
@Composable
fun DrawerHeader() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.launcher_icon_foreground),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
            }
            HorizontalDivider(
                Modifier.align(Alignment.BottomCenter), thickness = 1.dp,
                color = Color.DarkGray
            )
        }
}
@Composable
fun DrawerBody(
    items: List<ToolsData>,
    setTheme:(Boolean)->Unit,
    currentTheme:Boolean,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    navigateTo: (String) -> Unit
) {
    var checked by remember { mutableStateOf(currentTheme)}
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Switch(
            checked = checked,
            onCheckedChange = {
                setTheme(it)
                checked=it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(id =if(checked) R.string.dark_mode else R.string.light_mode),
            style = itemTextStyle,
            modifier = Modifier.weight(1f)
        )
    }
    LazyColumn {
        item{
            NavItem(navigateTo = navigateTo, item = ToolsData(
                R.drawable.language,
                R.string.language,
                Screens.Home.LanguagePickerScreen.route))
            NavItem(navigateTo = navigateTo, item = ToolsData(
                iconId=R.drawable.home,
                title = R.string.home,
                route= Screens.Home.route))
        }
        items(items) { item ->
           NavItem(navigateTo = navigateTo, item =item )
        }
    }
}
@Composable
fun NavItem(navigateTo: (String) -> Unit,
            item:ToolsData,
            itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp)){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateTo(item.route)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularIcon(iconResourceId = item.iconId,
                     backgroundColor = item.iconColor,
                     modifier = Modifier.size(40.dp),
                     iconSize = 25.dp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(id = item.title),
            style = itemTextStyle,
            modifier = Modifier.weight(1f)
        )
    }
}