package com.hencoder.arch.mvvm

import android.widget.EditText
import com.hencoder.arch.DataCenter

/**
 * ViewModel
 */
class ViewModel(data1View: EditText, data2View: EditText) {
  var data1: StringAttr = StringAttr()
  var data2: StringAttr = StringAttr()

  init {
    ViewBinder.bind(data1View, data1)
    ViewBinder.bind(data2View, data2)
  }

  fun init() {
    // 调度Model去获取数据
    val data = DataCenter.getData()
    // 更新内存中的数据，自动更新展示数据
    data1.value = data[0]
    data2.value = data[1]
  }
}