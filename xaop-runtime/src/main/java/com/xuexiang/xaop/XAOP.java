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

package com.xuexiang.xaop;

import android.app.Application;
import android.content.Context;

import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.PermissionUtils.OnPermissionDeniedListener;

/**
 * <pre>
 *     desc   : XAOP
 *     author : xuexiang
 *     time   : 2018/4/22 下午8:52
 * </pre>
 */
public final class XAOP {

    private static Context sContext;

    private static OnPermissionDeniedListener mOnPermissionDeniedListener;

    /**
     * 初始化
     * @param application
     */
    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    /**
     * 获取全局上下文
     * @return
     */
    public static Context getContext() {
        testInitialize();
        return sContext;
    }

    private static void testInitialize() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 XAOP.init() 初始化！");
        }
    }

    public static void setOnPermissionDeniedListener(OnPermissionDeniedListener listener) {
        XAOP.mOnPermissionDeniedListener = listener;
    }

    public static OnPermissionDeniedListener getOnPermissionDeniedListener() {
        return mOnPermissionDeniedListener;
    }

    /**
     * 设置是否打开调试
     *
     * @param isDebug
     */
    public static void debug(boolean isDebug) {
        XLogger.debug(isDebug);
    }

    /**
     * 设置调试模式
     *
     * @param tag
     */
    public static void debug(String tag) {
        XLogger.debug(tag);
    }

}
