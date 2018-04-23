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

package com.xuexiang.xaop.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     desc   : 应用的全局线程池 （包括单线程池的磁盘io，多线程池的网络io和主线程）
 *     author : xuexiang
 *     time   : 2018/4/20 下午4:52
 * </pre>
 */
public final class AppExecutors {

    private static AppExecutors sInstance;

    /**
     * 单线程池
     */
    private final ExecutorService mSingleIO;

    /**
     * 多线程池
     */
    private ExecutorService mPoolIO;

    /**
     * 主线程
     */
    private final Executor mMainThread;

    private AppExecutors(ExecutorService singleIO, ExecutorService poolIO, Executor mainThread) {
        mSingleIO = singleIO;
        mPoolIO = poolIO;
        mMainThread = mainThread;
    }

    /**
     * 更新多线程池
     * @param nThreads 线程池线程的数量
     * @return
     */
    public AppExecutors updatePoolIO(int nThreads) {
        mPoolIO = Executors.newFixedThreadPool(nThreads);
        return this;
    }

    private AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                new MainThreadExecutor());
    }

    /**
     * 获取线程管理实例
     *
     * @return
     */
    public static AppExecutors get() {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new AppExecutors();
                }
            }
        }
        return sInstance;
    }

    public ExecutorService singleIO() {
        return mSingleIO;
    }

    public ExecutorService poolIO() {
        return mPoolIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
