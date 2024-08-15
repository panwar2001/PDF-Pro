package com.panwar2001.pdfpro.ui.components

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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.model.DrawerItemData
import com.panwar2001.pdfpro.ui.DataSource
import com.panwar2001.pdfpro.ui.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AppModalNavigationDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope= rememberCoroutineScope(),
    navigateTo: (String) -> Unit,
    drawerItems:List<DrawerItemData> = remember { DataSource.DrawerItems },
    headerImageRes: Int,
    content: @Composable ()->Unit
){
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(Modifier.padding(0.dp,0.dp,60.dp,0.dp)) {
                DrawerHeader(headerImageRes)
                DrawerBody(items = drawerItems){
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
fun DrawerHeader(headerImageRes: Int) {
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
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_medium)))
                Image(
                    painter = painterResource(id = headerImageRes),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_large)))
                Text(
                    text = stringResource(id = R.string.app_name),
                    Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
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
    items: List<DrawerItemData>,
    navigateTo: (String) -> Unit
) {

    LazyColumn {
        items(items) { item ->
           NavItem(navigateTo = navigateTo, item =item){
               if(item.icon is Int) {
                   CircularIcon(
                       iconResourceId = item.icon as Int,
                       backgroundColor = Color(item.iconColor),
                       size = dimensionResource(id = R.dimen.icon_size_very_small)
                   )
               }else{
                   CircularIcon(
                       icon = item.icon as ImageVector,
                       backgroundColor = Color(item.iconColor),
                       size = dimensionResource(id = R.dimen.icon_size_very_small)
                   )
               }
           }
        }
    }
}
@Composable
fun NavItem(navigateTo: (String) -> Unit,
            item: DrawerItemData,
            iconComposable: @Composable ()->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigateTo(item.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_medium)))
        iconComposable()
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.spacing_medium)))
        Text(
            text = stringResource(id = item.title),
            style = MaterialTheme.typography.titleSmall
        )
    }
}
@Preview
@Composable
fun PreviewNavBar(drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Open)){
    val scope= rememberCoroutineScope()
    AppModalNavigationDrawer(
        drawerState,
        scope= scope,
        navigateTo ={},
        headerImageRes = R.drawable.pdf_icon
    ) {
        AppBar(onNavigationIconClick = {
            scope.launch { drawerState.open() }
        })
    }
}