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

package com.xuexiang.xaop.aspectj;

import android.os.Looper;

import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.AppExecutors;
import com.xuexiang.xaop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.concurrent.Callable;

/**
 * <pre>
 *     desc   : 子线程切片, 保证注解的方法发生在子线程中
 *     author : xuexiang
 *     time   : 2018/4/23 上午1:14
 * </pre>
 */
@Aspect
public class IOThreadAspectJ {


    @Pointcut("within(@com.xuexiang.xaop.annotation.IOThread *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.xuexiang.xaop.annotation.IOThread * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(ioThread)")//在连接点进行方法替换
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, IOThread ioThread) throws Throwable {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return joinPoint.proceed();
        } else {
            XLogger.d(Utils.getMethodDescribeInfo(joinPoint) + " \u21E2 [当前线程]:" + Thread.currentThread().getName() + "，正在切换到子线程！");
            Object result = null;
            switch(ioThread.value()) {
                case Single:
                case Disk:
                    result = AppExecutors.get().singleIO().submit(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return getProceedResult(joinPoint);
                        }
                    }).get();
                    break;
                case Fixed:
                case Network:
                    result = AppExecutors.get().poolIO().submit(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return getProceedResult(joinPoint);
                        }
                    }).get();
                    break;
            }
            XLogger.d(Utils.getMethodDescribeInfo(joinPoint) + " \u21E0 [执行结果]:" + Utils.toString(result));
            return result;
        }
    }

    /**
     * 获取执行结果
     * @param joinPoint
     */
    private Object getProceedResult(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            XLogger.e(e);
        }
        return null;
    }
}
