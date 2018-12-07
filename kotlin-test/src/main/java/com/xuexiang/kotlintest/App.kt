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

import android.app.Application
import com.xuexiang.kotlintest.MainActivity.Companion.TRY_CATCH_KEY
import com.xuexiang.xaop.XAOP
import com.xuexiang.xaop.checker.IThrowableHandler
import com.xuexiang.xaop.checker.Interceptor
import com.xuexiang.xaop.logger.XLogger
import com.xuexiang.xaop.util.Utils
import com.xuexiang.xutil.XUtil
import com.xuexiang.xutil.tip.ToastUtils

/**
 * <pre>
 *     desc   :
 *     author : xuexiang
 *     time   : 2018/5/2 上午12:17
 * </pre>
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        XUtil.init(this)

        XAOP.init(this)
        XAOP.debug(true)
//        XAOP.setPriority(Log.INFO);
        XAOP.setOnPermissionDeniedListener { permissionsDenied ->
            /**
             * 权限申请被拒绝
             *
             * @param permissionsDenied
             */
            ToastUtils.toast("权限申请被拒绝:" + Utils.listToString(permissionsDenied))
        }

        XAOP.setInterceptor(Interceptor { type, _ ->
            XLogger.d("正在进行拦截，拦截类型:$type")
            when (type) {
                1 -> {
                }
                2 -> return@Interceptor true //return true，直接拦截切片的执行
                else -> {
                }
            }//做你想要的拦截
            false
        })

        XAOP.setIThrowableHandler { flag, throwable ->
            XLogger.d("捕获到异常，异常的flag:$flag")
            if (flag == TRY_CATCH_KEY) {
                100
            } else null
        }
    }
}