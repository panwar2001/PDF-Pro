package com.panwar2001.pdfpro.ui
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.WorkerThread
import com.panwar2001.pdfpro.R
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import java.util.Locale

@WorkerThread
private fun getCurrentLocale(context: Context): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
    } else {
        AppCompatDelegate.getApplicationLocales().toLanguageTags()
    }
}

/**
 * TODO:  check for how to set locale for less than android 13 version
 */
@WorkerThread
private fun setLocale(localeTag: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales=  LocaleList(Locale.forLanguageTag(localeTag))
    } else {
    AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(
                localeTag
            )
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePickerScreen(navigateUp:()->Unit){
    val context= LocalContext.current
    val currentLocale= getCurrentLocale(context)
    val languages= listOf(
        stringArrayResource(id = R.array.english),
        stringArrayResource(id = R.array.French),
        stringArrayResource(id = R.array.Japanese),
        stringArrayResource(id = R.array.Russian),
        stringArrayResource(id = R.array.hindi)
    )
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.language),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1)},
            navigationIcon = {
                IconButton(onClick = {navigateUp()}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
    }){
        LazyColumn(modifier = Modifier.padding(it)){
            items(languages) {language->
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp)){
                    RadioButton(selected = currentLocale == language[0],
                        enabled = currentLocale != language[0],
                        onClick = {
                            setLocale(language[0],context)
                        })
                    Text(text = "${language[1]} - ${language[0]}")
                    if(language[2].isNotEmpty()) {
                        Text(text = "  (${language[2]})")
                    }
                }
            }
        }
    }
}