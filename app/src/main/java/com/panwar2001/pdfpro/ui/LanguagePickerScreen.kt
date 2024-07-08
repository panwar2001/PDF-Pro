package com.panwar2001.pdfpro.ui

import com.panwar2001.pdfpro.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagePickerScreen(navigateUp:()->Unit,
                         languages:List<Int>,
                         currentLocale:String,
                         setLocale:(localeTag:String)->Unit){
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.language),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1)},
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
            items(languages) {languageId->
                val language=stringArrayResource(id = languageId)
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 20.dp)){
                    RadioButton(selected = currentLocale == language[0],
                        enabled = currentLocale != language[0],
                        onClick = {
                            setLocale(language[0])
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
@Preview
@Composable
fun PreviewScreen(){
    val languages = listOf(
        R.array.english,
        R.array.French,
        R.array.Japanese,
        R.array.Russian,
        R.array.hindi)

    LanguagePickerScreen(navigateUp = { /*TODO*/ },
                         languages = languages,
                         currentLocale = "en") {}
}