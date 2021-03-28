package com.hencoder.scalableimageview

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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


val Int.half: Int
    get() = (this / 2F).toInt()

val Float.radians: Float
    get() = Math.toRadians(this.toDouble()).toFloat()

fun getAvatar(res: Resources, width: Int): Bitmap {
  val options = BitmapFactory.Options()
  options.inJustDecodeBounds = true
  BitmapFactory.decodeResource(res, R.drawable.avatar_rengwuxian, options)
  options.inJustDecodeBounds = false
  options.inDensity = options.outWidth
  options.inTargetDensity = width
  return BitmapFactory.decodeResource(res, R.drawable.avatar_rengwuxian, options)
}