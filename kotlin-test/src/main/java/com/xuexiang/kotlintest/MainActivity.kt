/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xuexiang.kotlintest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.xuexiang.xaop.annotation.DebugLog
import com.xuexiang.xaop.annotation.DiskCache
import com.xuexiang.xaop.annotation.IOThread
import com.xuexiang.xaop.annotation.Intercept
import com.xuexiang.xaop.annotation.MainThread
import com.xuexiang.xaop.annotation.MemoryCache
import com.xuexiang.xaop.annotation.Permission
import com.xuexiang.xaop.annotation.Safe
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xaop.consts.PermissionConsts
import com.xuexiang.xaop.enums.ThreadType
import com.xuexiang.xaop.logger.XLogger
import com.xuexiang.xutil.system.AppExecutors
import com.xuexiang.xutil.tip.ToastUtils

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mTvHello: TextView

    private val number: Int
        @Safe(TRY_CATCH_KEY)
        get() = 100 / 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvHello = findViewById(R.id.tv_hello)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_single_click -> handleOnClick(v)
            R.id.btn_request_permission -> handleRequestPermission(v)
            R.id.btn_main_thread -> AppExecutors.get().networkIO().execute { doInMainThread(v) }
            R.id.btn_io_thread -> doInIOThread(v)
            R.id.btn_try_catch -> {
                val result = number
                mTvHello.text = "结果为:$result"
            }
            R.id.btn_lambda -> AppExecutors.get().networkIO().execute { doInMainThread(v) }
            else -> {
            }
        }
    }

    @SingleClick
    @Permission(PermissionConsts.CALENDAR, PermissionConsts.CAMERA, PermissionConsts.LOCATION)
    private fun handleRequestPermission(v: View) {

    }

    @SingleClick(5000)
    @DebugLog(priority = Log.ERROR)
    @Intercept(3)
    fun handleOnClick(v: View) {
        XLogger.e("点击响应！")
        ToastUtils.toast("点击响应！")
        hello("xuexiangjys", "666666")
    }


    @DebugLog(priority = Log.ERROR)
    @Intercept(1, 2, 3)
    //    @MemoryCache
    @DiskCache
    private fun hello(name: String, cardId: String): String {
        return "hello, $name! Your CardId is $cardId."
    }


    @MainThread
    private fun doInMainThread(v: View) {
        mTvHello.text = "工作在主线程"
    }

    @IOThread(ThreadType.Single)
    @MemoryCache("12345")
    private//    @DiskCache
    fun doInIOThread(v: View): String {
        return "io线程名:" + Thread.currentThread().name
    }

    companion object {
        const val TRY_CATCH_KEY = "getNumber"
    }
}