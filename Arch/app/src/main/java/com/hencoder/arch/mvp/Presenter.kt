package com.hencoder.arch.mvp

import com.hencoder.arch.DataCenter

/**
 * Presenter
 */
class Presenter(private val view: IView) {
  fun init() {
    // 调度Model去获取数据
    val data = DataCenter.getData()
    // 调度View去展示数据
    view.showData(data)
  }

  /**
   * View的抽象接口，面向接口编程
   */
  interface IView {
    fun showData(data: List<String>)
  }
}