package com.hencoder.touch;

import android.view.MotionEvent;

/**
 * 触摸事件流程
 */
class Touch {

    class View {
        public boolean dispatchTouchEvent(MotionEvent event) {
            return onTouchEvent(event);
        }

        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }
    }

    class ViewGroup {
        public boolean dispatchTouchEvent(MotionEvent event) {
            boolean result = false;
            if (onInterceptTouchEvent(event)) {
                // 这里实际调的是super.dispatchTouchEvent(event)->内部调用onTouchEvent(event)
                result = onTouchEvent(event);
            } else {
                // result = 子View的dispatchTouchEvent(event);
            }
            return result;
        }

        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return false;
        }

        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }
    }

    class Activity {
        public boolean dispatchTouchEvent(MotionEvent event) {
            // 递归调用ViewGroup.dispatchTouchEvent(event)
            // 如果都没有返回true,那么最终回调自己的onTouchEvent(event)
            return onTouchEvent(event);
        }
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }
    }

}
