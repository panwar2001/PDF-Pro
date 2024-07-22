package com.panwar2001.pdfpro.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.panwar2001.pdfpro.R

enum class SnackbarType {
    SUCCESS,
    ERROR
}
@Composable
fun CustomSnackbar(title: String, description: String, type: SnackbarType) {
    val (backgroundColor, textColor) = when (type) {
        SnackbarType.ERROR -> Pair(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.error
        )

        SnackbarType.SUCCESS -> Pair(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.error
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ),

        ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.Center,
        ) {

            VerticalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 8.dp,
                modifier = Modifier
                    .padding(all = 0.dp)
                    .fillMaxHeight()
            )

            Column(modifier = Modifier.padding(all = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (icon, tint) = when (type) {
                        SnackbarType.ERROR -> Pair(
                            R.drawable.lock_pdf,
                            MaterialTheme.colorScheme.error
                        )

                        SnackbarType.SUCCESS -> Pair(
                            R.drawable.lock_pdf,
                            MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "Status Icon",
                        tint = tint
                    )

                    Spacer(modifier = Modifier.padding(all = 4.dp))
                    Text(text = title, color = textColor)
                }
                Spacer(modifier = Modifier.padding(all = 4.dp))
                Text(text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4)
            }
        }
    }
}