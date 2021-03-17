package com.hencoder.materialedittext.practice

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.hencoder.materialedittext.R
import com.hencoder.materialedittext.dp

private val TEXT_SIZE = 12.dp
private val TEXT_MARGIN = 8.dp
private val HORIZONTAL_OFFSET = 5.dp
private val VERTICAL_OFFSET = 23.dp
private val EXTRA_VERTICAL_OFFSET = 16.dp

class MaterialEditText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
    }

    // 用户辅助判断悬浮提示是否展示
    private var isFloatingLabelShowing = true

    // 用户控制浮提示动画
    var floatingLabelFraction = 1f
        set(value) {
            field = value
            invalidate()
        }

    init {
        // 增加额外的绘制区域
        setPadding(paddingLeft, (paddingTop + TEXT_SIZE + TEXT_MARGIN).toInt(), paddingRight, paddingBottom)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 监听文字变化,进行动画
                if (!s.isNullOrEmpty() && isFloatingLabelShowing) {
                    isFloatingLabelShowing = false
                    Toast.makeText(context, "消失", Toast.LENGTH_SHORT).show()
                    val animator = ObjectAnimator.ofFloat(this@MaterialEditText, "floatingLabelFraction", 1F, 0F)
                    animator.start()
                } else if (s.isNullOrEmpty() && !isFloatingLabelShowing) {
                    isFloatingLabelShowing = true
                    Toast.makeText(context, "出现", Toast.LENGTH_SHORT).show()
                    val animator = ObjectAnimator.ofFloat(this@MaterialEditText, "floatingLabelFraction", 0F, 1F)
                    animator.start()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.alpha = (floatingLabelFraction * 0xff).toInt()
        // 绘制文字
        canvas.drawText(hint.toString(), HORIZONTAL_OFFSET, VERTICAL_OFFSET, paint)
    }

}