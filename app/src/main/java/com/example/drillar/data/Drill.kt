package com.example.drillar.data

import androidx.annotation.DrawableRes

data class Drill(
    val id: String,
    val name: String,
    val description: String,
    val tips: String,
    @DrawableRes val imageResId: Int? = null
)
