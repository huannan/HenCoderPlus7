package com.hencoder.layoutprocess.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

class OneHundredView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    // 在测量阶段,无视父view的要求(结合了开发者的要求),强行指定自己的期望尺寸
    // 如果父View是ConstraintLayout,尺寸会被强行修正回来
    setMeasuredDimension(100, 100)
  }

  override fun layout(l: Int, t: Int, r: Int, b: Int) {
    // 在布局阶段,通过重写layout方法,强行指定自己的尺寸或者位置
    // 如果父View是ConstraintLayout,此时已经过了父View的layout阶段了,父View已经无法控制子VIew的尺寸和位置了
    super.layout(l, t, l + 100, t + 100)
  }

  /**
   * 这个是调度子View进行内部布局的方法,View的实现是空的
   */
  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
  }
}