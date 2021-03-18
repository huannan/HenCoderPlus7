package com.hencoder.materialedittext.practice

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.hencoder.materialedittext.R
import com.hencoder.materialedittext.dp

private val TEXT_SIZE = 12.dp
private val TEXT_MARGIN = 8.dp
private val HORIZONTAL_OFFSET = 5.dp
private val VERTICAL_OFFSET = 23.dp

class MaterialEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = TEXT_SIZE
        }
    }

    // 用户辅助判断悬浮提示是否展示
    private var isFloatingLabelShowing = true

    // 用户控制浮提示动画
    var floatingLabelFraction = 1F
        set(value) {
            field = value
            invalidate()
        }
    private val animator by lazy {
        ObjectAnimator.ofFloat(this@MaterialEditText, "floatingLabelFraction", 1F, 0F)
    }

    // 控制是否展示浮提示
    var useFloatingLabel = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    // 增加额外的绘制区域
                    setPadding(paddingLeft, (paddingTop + TEXT_SIZE + TEXT_MARGIN).toInt(), paddingRight, paddingBottom)
                } else {
                    setPadding(paddingLeft, (paddingTop - TEXT_SIZE - TEXT_MARGIN).toInt(), paddingRight, paddingBottom)
                }
            }
        }

    init {
        // attrs是来自xml里面每一个属性的键值对
        // 过滤属性:即用R.styleable.MaterialEditText来对attrs进行过滤
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText)
        // 根据index获取属性值,index是在R.styleable.MaterialEditText里面的
        useFloatingLabel = typeArray.getBoolean(R.styleable.MaterialEditText_useFloatingLabel, false)
        // 回收资源
        typeArray.recycle()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!useFloatingLabel) {
                    return
                }
                // 监听文字变化,进行动画
                if (!s.isNullOrEmpty() && isFloatingLabelShowing) {
                    isFloatingLabelShowing = false
                    // 消失
                    animator.start()
                } else if (s.isNullOrEmpty() && !isFloatingLabelShowing) {
                    isFloatingLabelShowing = true
                    // 出现
                    animator.reverse()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!useFloatingLabel) {
            return
        }
        paint.alpha = (floatingLabelFraction * 0xFF).toInt()
        // 绘制文字
        val verticalOffset = VERTICAL_OFFSET + (1F - floatingLabelFraction) * 16.dp
        canvas.drawText(hint.toString(), HORIZONTAL_OFFSET, verticalOffset, paint)
    }

}