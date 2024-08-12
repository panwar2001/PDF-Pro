package com.panwar2001.pdfpro.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

/** Display [BannerAd]
 *
 * Check if the current composition is being rendered in a preview environment.
 * If there is preview environment ,then display a mock Row inplace of actual Ad
 * else display the actual Ad
 *
 * @param adUnitResID the string resource id of the unit id of banner ad
 */
@Composable
fun BannerAd(modifier: Modifier = Modifier,@StringRes adUnitResID:Int) {
    if(LocalInspectionMode.current){
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .background(color = Color.Gray)
                .padding(32.dp)
                .fillMaxWidth()){
              Text(text = "Ads are displayed here")
        }
    }else {
        val adUnitID = stringResource(id = adUnitResID)
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = adUnitID
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}
