package com.example.motionlayout

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionManager

class ObjectAnimator2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_animator2)
    }

    fun onClick(v: View) {
        // 属性动画不会改变layoutParams,当然可以通过自定义属性动画来实现
        /*
        v.animate()
                .scaleX(2F)
                .scaleY(2F)
                .start()
        */

        TransitionManager.beginDelayedTransition(v.parent as ViewGroup)
        with(v.layoutParams as LinearLayout.LayoutParams) {
            height *= 2
            width *= 2
        }
    }
}
