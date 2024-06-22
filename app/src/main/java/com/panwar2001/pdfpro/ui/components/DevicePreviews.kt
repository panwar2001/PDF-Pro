package com.panwar2001.pdfpro.ui.components

import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Pixel 5",
         device = "spec:shape=Normal,width=1080,height=2400,unit=px,dpi=480",
         showSystemUi = true)
@Preview(name = "Samsung Galaxy S21 Ultra",
         device = "spec:shape=Normal,width=1440,height=3200,unit=px,dpi=515",
         showSystemUi = true)
@Preview(name = "Foldable",
         device = Devices.FOLDABLE,
         showSystemUi = true)

annotation class DevicePreviews