package com.hencoder.text

import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue

val Float.dp: Float
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics)

val Int.dp: Float
    get() = this.toFloat().dp

val Float.half: Float
    get() = this / 2F


val Int.half: Float
    get() = this / 2F

val Float.radians: Float
    get() = Math.toRadians(this.toDouble()).toFloat()

val String.color: Int
    get() = Color.parseColor(this)