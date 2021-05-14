package com.hencoder.arch.mvc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hencoder.arch.DataCenter
import com.hencoder.arch.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.data1View
import kotlinx.android.synthetic.main.activity_main.data2View
import kotlinx.android.synthetic.main.activity_main_mvc.*

/**
 * Controller
 */
class MvcActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main_mvc)

    // 调度Model去获取数据
    val data = DataCenter.getData()
    // 调度View去展示数据
    dataView.showData(data)
  }
}