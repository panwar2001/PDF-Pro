package com.panwar2001.pdfpro.usecase

import javax.inject.Inject

class GetFileSizeUseCase @Inject constructor() {
    operator fun invoke(len:Long?): String=when {
        len==null-> "0 Byte"
        len < 1024 -> "$len Byte"
        len < 1048576 -> "%.1f KB".format(len / 1024.0)
        len < 1073741824 -> "%.1f MB".format(len / 1048576.0)
        else -> "%.1f GB".format(len / 1073741824.0)
    }
}