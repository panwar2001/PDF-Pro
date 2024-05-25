package com.panwar2001.pdfpro.ui.components

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.pdfpro.R
import com.panwar2001.pdfpro.data.MenuItem
import org.bouncycastle.crypto.agreement.kdf.ConcatenationKDFGenerator
import java.security.AccessController.getContext


@Composable
fun DrawerHeader(modifier: Modifier=Modifier) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier.wrapContentSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.launcher_icon_foreground),
                    contentDescription = "profile pic",
                    modifier = modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                modifier.align(Alignment.BottomCenter), thickness = 1.dp,
                Color.DarkGray
            )
        }
}
@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier=Modifier ,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    setTheme:(Boolean)->Unit,
    currentTheme:Boolean,
    navigateTo: (String) -> Unit
) {
    var checked by remember { mutableStateOf(currentTheme)}
    Row(
        modifier = modifier
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
        Spacer(modifier = modifier.width(16.dp))
        Text(
            text = if(checked) "Dark Theme" else "Light Theme",
            style = itemTextStyle,
            modifier = modifier.weight(1f)
        )
    }
    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateTo(item.screen)
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
                ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = item.contentDescription,
                    modifier=modifier.size(30.dp)
                )
                Spacer(modifier = modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = modifier.weight(1f)
                )
            }
        }
    }
}