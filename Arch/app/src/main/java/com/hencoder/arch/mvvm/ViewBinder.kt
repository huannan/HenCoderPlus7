package com.hencoder.arch.mvvm

import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged

class ViewBinder {
  companion object {
    private const val TAG = "MVVM"

    fun bind(editText: EditText, stringAttr: StringAttr) {
      editText.doAfterTextChanged {
        if (!TextUtils.equals(stringAttr.value, it)) {
          stringAttr.value = it.toString()
          Log.d(TAG, "表现数据通知内存啦！${it}")
        }
      }
      stringAttr.onChangeListener = object : StringAttr.OnChangeListener {
        override fun onChange(newValue: String?) {
          if (!TextUtils.equals(newValue, editText.text)) {
            editText.setText(newValue)
            Log.d(TAG, "内存通知表现数据啦！${newValue}")
          }
        }
      }
    }
  }
}