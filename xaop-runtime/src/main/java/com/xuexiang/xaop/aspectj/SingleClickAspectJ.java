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

import android.view.View;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.ClickUtils;
import com.xuexiang.xaop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

/**
 * <pre>
 *     desc   : 防止View被连续点击
 *     author : xuexiang
 *     time   : 2018/4/22 下午6:38
 * </pre>
 */
@Aspect
public class SingleClickAspectJ {

    @Pointcut("within(@com.xuexiang.xaop.annotation.SingleClick *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.xuexiang.xaop.annotation.SingleClick * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(singleClick)")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, SingleClick singleClick) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) {
                view = (View) arg;
                break;
            }
        }
        if (view != null) {
            if (!ClickUtils.isFastDoubleClick(view, singleClick.value())) {
                joinPoint.proceed();//不是快速点击，执行原方法
            } else {
                XLogger.d(Utils.getMethodDescribeInfo(joinPoint) + ":发生快速点击，View id:" + view.getId());
            }
        }
    }




}
