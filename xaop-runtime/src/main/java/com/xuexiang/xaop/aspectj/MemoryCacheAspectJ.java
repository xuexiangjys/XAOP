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

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xaop.cache.XMemoryCache;
import com.xuexiang.xaop.logger.XLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Collection;

/**
 * <pre>
 *     desc   : 内存缓存切片
 *     author : xuexiang
 *     time   : 2018/4/23 下午11:52
 * </pre>
 */
@Aspect
public class MemoryCacheAspectJ {
    @Pointcut("within(@com.xuexiang.xaop.annotation.MemoryCache *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.xuexiang.xaop.annotation.MemoryCache * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(memoryCache)")//在连接点进行方法替换
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, MemoryCache memoryCache) throws Throwable {
        if (!isHasReturnType(joinPoint.getSignature())) return joinPoint.proceed(); //没有返回值的方法，不进行缓存处理

        String key = memoryCache.value();
        if (TextUtils.isEmpty(key)) {
            key = getCacheKey(joinPoint);
        }
        Object result = XMemoryCache.getInstance().load(key);
        XLogger.iTag("MemoryCache", "key：" + key + "--->" + (result != null ? "not null, do not need to proceed method " + joinPoint.getSignature().getName() : "null, need to proceed method " + joinPoint.getSignature().getName()));
        if (result != null) return result;//缓存已有，直接返回

        result = joinPoint.proceed();//执行原方法
        if (result != null) {
            if (result instanceof Collection && !((Collection) result).isEmpty() //列表不为空
                    || result instanceof String && !TextUtils.isEmpty((String) result)) { //字符不为空
                XMemoryCache.getInstance().save(key, result);//存入缓存
                XLogger.iTag("MemoryCache", "key：" + key + "--->" + "save ");
            }
        }
        return result;
    }

    /**
     * 获取缓存的key
     * <p>key规则 ： 方法名＋参数1+参数2+...</p>
     *
     * @param joinPoint
     * @return
     */
    @NonNull
    private String getCacheKey(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(methodName);
        for (Object obj : joinPoint.getArgs()) {
            if (obj instanceof String) keyBuilder.append((String) obj);
            else if (obj instanceof Class) keyBuilder.append(((Class) obj).getSimpleName());
        }
        return keyBuilder.toString();
    }

    /**
     * 方法是否有返回值
     *
     * @param signature
     * @return
     */
    private boolean isHasReturnType(Signature signature) {
        return signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;
    }
}
