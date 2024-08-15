package com.panwar2001.pdfpro.feature.settings

import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.screens.Screens

private const val feedback_url= "https://forms.gle/9xJLouAgBdgpmcKs8"
private const val privacy_policy_url = "https://sites.google.com/view/privacy-policy-pdfpro/home"



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navigateUp: ()->Unit,
                  navigateTo:(String)->Unit,
                  settings : SettingsUiState.Success,
                  setTheme : (Boolean)->Unit){
    var isDarkTheme by remember { mutableStateOf(settings.isDarkTheme) }
    val padding= dimensionResource(com.panwar2001.pdfpro.ui.R.dimen.spacing_medium)
    val uriHandler = LocalUriHandler.current
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(
                text = stringResource(id = R.string.settings),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1)
                    },
            navigationIcon = {
                IconButton(onClick = {navigateUp()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
    }){
        LazyColumn(modifier = Modifier.padding(it)){
            item {
                SettingsItem(
                    modifier = Modifier.padding(padding),
                    iconResId = R.drawable.language,
                    urlResId = R.array.language,
                    onClick = { navigateTo(Screens.Settings.LanguagePickerScreen.route) }
                )
            }
            item {
               SwitchRow(
                   modifier = Modifier.padding(padding),
                   currentTheme = isDarkTheme,
                   onCheckedChange = {theme->
                       isDarkTheme=theme
                       setTheme(isDarkTheme)
                   },
                   itemResId= R.array.app_theme,
                   iconResId =  if(isDarkTheme) R.drawable.dark_theme else R.drawable.light_theme
               )
           }
            item {
                SettingsItem(
                    modifier = Modifier.padding(padding),
                    iconResId = R.drawable.feedback,
                    urlResId = R.array.feedback_google_form_content,
                    onClick ={ uriHandler.openUri(feedback_url)}
                )
            }
            item {
                SettingsItem(
                    modifier = Modifier.padding(padding),
                    iconResId = R.drawable.feature_settings_privacy_policy,
                    urlResId = R.array.privacy_policy,
                    onClick = { uriHandler.openUri(privacy_policy_url)}
                )
            }
            item {
                AppVersionItem(appVersion = settings.appVersion)
            }
        }
    }
}

@Composable
fun SettingsItem(
    modifier: Modifier=Modifier,
    @ArrayRes urlResId: Int,
    @DrawableRes iconResId: Int,
    onClick: ()->Unit,
    ){
    val resource= stringArrayResource(id = urlResId)
    Row(modifier = modifier.clickable {onClick()},
        verticalAlignment = Alignment.CenterVertically){
        Content(
            iconResId = iconResId,
            title = resource[0],
            description = resource[1])
    }
}
@Composable
fun Content(
    modifier: Modifier= Modifier,
    @DrawableRes iconResId: Int,
    title: String,
    description: String
){
    val spacingSmall= dimensionResource(com.panwar2001.pdfpro.ui.R.dimen.spacing_small)
    val spacingMedium= dimensionResource(com.panwar2001.pdfpro.ui.R.dimen.spacing_medium)

    Spacer(modifier.width(spacingSmall))
    Icon(modifier = Modifier.size(24.dp), painter = painterResource(iconResId),contentDescription =  null)
    Spacer(modifier.width(spacingMedium))
    Column {
        Text(text = title,
            style = MaterialTheme.typography.titleMedium)
        Text(text = description,
            style = MaterialTheme.typography.bodySmall)
    }
    Spacer(modifier.width(spacingSmall))
}
@Composable
fun SwitchRow(modifier: Modifier = Modifier,
              currentTheme:Boolean,
              @DrawableRes iconResId: Int,
              onCheckedChange:(Boolean)->Unit,
              @ArrayRes itemResId: Int){
    val resource= stringArrayResource(id = itemResId)
    Row(modifier= modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically){
        Row(Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically){
            Content(
                iconResId = iconResId,
                title = resource[0],
                description = resource[1])
            }
        Row {
            Switch(
                checked = currentTheme,
                onCheckedChange = {
                    onCheckedChange(it)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                    uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            )
        }
    }
}

@Composable
fun AppVersionItem(modifier: Modifier=Modifier,
                   appVersion: String){
    HorizontalDivider()
    val version= stringResource(id = R.string.version,appVersion)
    Row(modifier = modifier
        .padding(dimensionResource(com.panwar2001.pdfpro.ui.R.dimen.spacing_medium))
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Text(text = version,
            style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
@Preview
fun PreviewScreen(){
    SettingScreen(
        navigateUp = {},
        settings = SettingsUiState.Success(true,"1.0.0"),
        navigateTo = {},
        setTheme = {}
    )
}