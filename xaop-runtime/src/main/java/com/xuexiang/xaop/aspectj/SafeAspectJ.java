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
import android.text.TextUtils;

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.annotation.Safe;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.AppExecutors;
import com.xuexiang.xaop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <pre>
 *     desc   : 自动try-catch的注解切片处理
 *     author : xuexiang
 *     time   : 2018/5/14 下午10:39
 * </pre>
 */
@Aspect
public class SafeAspectJ {

    @Pointcut("within(@com.xuexiang.xaop.annotation.Safe *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.xuexiang.xaop.annotation.Safe * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(safe)")//在连接点进行方法替换
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, Safe safe) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            if (XAOP.getIThrowableHandler() != null) {
                String flag = safe.value();
                if (TextUtils.isEmpty(flag)) {
                    flag = Utils.getMethodName(joinPoint);
                }
                result = XAOP.getIThrowableHandler().handleThrowable(flag, e);
            } else {
                XLogger.e(e); //默认不做任何处理
            }
        }
        return result;
    }
}
