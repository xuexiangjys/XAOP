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

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.annotation.Intercept;
import com.xuexiang.xaop.logger.XLogger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <pre>
 *     desc   : 自定义拦截切片
 *     author : xuexiang
 *     time   : 2018/4/24 下午10:01
 * </pre>
 */
@Aspect
public class InterceptAspectJ {

    @Pointcut("within(@com.xuexiang.xaop.annotation.Intercept *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.xuexiang.xaop.annotation.Intercept * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    } //方法切入点

    @Pointcut("execution(@com.xuexiang.xaop.annotation.Intercept *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    } //构造器切入点

    @Around("(method() || constructor()) && @annotation(intercept)")
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, Intercept intercept) throws Throwable {
        if (XAOP.getInterceptor() == null) return joinPoint.proceed(); //没有拦截器不执行切片拦截
        //执行拦截操作
        boolean result = proceedIntercept(intercept.value(), joinPoint);
        XLogger.d("拦截结果:" + result + ", 切片" + (result ? "被拦截！" : "正常执行！"));
        return result ? null : joinPoint.proceed();
    }

    /**
     * 执行拦截操作
     *
     * @param types     拦截的类型集合
     * @param joinPoint 切片
     * @return {@code true}: 拦截切片的执行 <br>{@code false}: 不拦截切片的执行
     */
    private boolean proceedIntercept(int[] types, JoinPoint joinPoint) throws Throwable {
        for (int type : types) {
            if (XAOP.getInterceptor().intercept(type, joinPoint)) { //拦截执行
                return true;
            }
        }
        return false;
    }

}
