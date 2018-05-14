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
import android.support.annotation.NonNull;

import com.xuexiang.xaop.cache.XCache;
import com.xuexiang.xaop.cache.XDiskCache;
import com.xuexiang.xaop.cache.XMemoryCache;
import com.xuexiang.xaop.checker.IThrowableHandler;
import com.xuexiang.xaop.checker.Interceptor;
import com.xuexiang.xaop.logger.ILogger;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.PermissionUtils.OnPermissionDeniedListener;
import com.xuexiang.xaop.util.Strings;

/**
 * <pre>
 *     desc   : XAOP
 *     author : xuexiang
 *     time   : 2018/4/22 下午8:52
 * </pre>
 */
public final class XAOP {

    private static Context sContext;

    /**
     * 权限申请被拒绝的监听
     */
    private static OnPermissionDeniedListener sOnPermissionDeniedListener;

    /**
     * 自定义拦截切片的拦截器接口
     */
    private static Interceptor sInterceptor;

    /**
     * 自定义的异常处理者接口
     */
    private static IThrowableHandler sIThrowableHandler;
    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    /**
     * 获取全局上下文
     *
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

    //============动态申请权限失败事件设置=============//
    /**
     * 设置权限申请被拒绝的监听
     *
     * @param listener 权限申请被拒绝的监听器
     */
    public static void setOnPermissionDeniedListener(OnPermissionDeniedListener listener) {
        XAOP.sOnPermissionDeniedListener = listener;
    }

    public static OnPermissionDeniedListener getOnPermissionDeniedListener() {
        return sOnPermissionDeniedListener;
    }

    //============自定义拦截器设置=============//
    /**
     * 设置自定义拦截切片的拦截器接口
     *
     * @param sInterceptor 自定义拦截切片的拦截器接口
     */
    public static void setInterceptor(Interceptor sInterceptor) {
        XAOP.sInterceptor = sInterceptor;
    }

    public static Interceptor getInterceptor() {
        return sInterceptor;
    }

    //============自定义捕获异常处理=============//

    /**
     * 设置自定义捕获异常处理
     * @param sIThrowableHandler 自定义捕获异常处理
     */
    public static void setIThrowableHandler(IThrowableHandler sIThrowableHandler) {
        XAOP.sIThrowableHandler = sIThrowableHandler;
    }

    public static IThrowableHandler getIThrowableHandler() {
        return sIThrowableHandler;
    }

    //============日志打印设置=============//
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

    /**
     * 设置打印日志的等级（只打印改等级以上的日志）
     *
     * @param priority
     */
    public static void setPriority(int priority) {
        XLogger.setPriority(priority);
    }

    /**
     * 设置日志打印时参数序列化的接口方法
     *
     * @param sISerializer
     */
    public static void setISerializer(Strings.ISerializer sISerializer) {
        XLogger.setISerializer(sISerializer);
    }

    /**
     * 设置日志记录者的接口
     *
     * @param logger
     */
    public static void setLogger(@NonNull ILogger logger) {
        XLogger.setLogger(logger);
    }

    //============缓存设置=============//
    /**
     * 初始化内存缓存
     *
     * @param memoryMaxSize
     */
    public static void initMemoryCache(int memoryMaxSize) {
        XMemoryCache.getInstance().init(memoryMaxSize);
    }

    /**
     * 初始化磁盘缓存
     *
     * @param builder
     */
    public static void initDiskCache(XCache.Builder builder) {
        XDiskCache.getInstance().init(builder);
    }


}
