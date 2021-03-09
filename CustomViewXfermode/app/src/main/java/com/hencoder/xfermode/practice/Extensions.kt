package com.hencoder.xfermode.practice

import android.content.res.Resources
import android.util.TypedValue

val Float.dp: Float
    get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics)

val Float.half: Float
    get() = this / 2F

val Float.radians: Float
    get() = Math.toRadians(this.toDouble()).toFloat()