package com.panwar2001.pdfpro.model

/**
 *  This data class represents the tool data mentioned in home screen
 *
 *  @property iconId stores icon id of resources
 *  @property title stores description of a particular tool
 */
data class ToolsData(
    val iconId: Int,
    val title: Int,
    val route: String,
    val iconColor: Long
)
