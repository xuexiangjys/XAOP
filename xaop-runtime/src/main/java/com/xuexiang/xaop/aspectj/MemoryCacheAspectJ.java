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

import android.text.TextUtils;

import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xaop.cache.XMemoryCache;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

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
        if (!Utils.isHasReturnType(joinPoint.getSignature())) {
            return joinPoint.proceed(); //没有返回值的方法，不进行缓存处理
        }

        String key = memoryCache.value();
        if (TextUtils.isEmpty(key)) {
            key = Utils.getCacheKey(joinPoint);
        }
        Object result = XMemoryCache.getInstance().load(key);
        XLogger.dTag("MemoryCache", getCacheMsg(joinPoint, key, result));
        if (result != null) {
            if (memoryCache.enableEmpty() || !Utils.isEmpty(result)) {
                //缓存已有，直接返回
                return result;
            }
        }

        result = joinPoint.proceed();//执行原方法
        if (result != null) {
            if (memoryCache.enableEmpty() || !Utils.isEmpty(result)) {
                saveResult(key, result);
            }
        }
        return result;
    }

    /**
     * 保存结果
     *
     * @param key
     * @param result
     */
    private void saveResult(String key, Object result) {
        XMemoryCache.getInstance().save(key, result);//存入缓存
        XLogger.dTag("MemoryCache", "key：" + key + "--->" + "save ");
    }

    /**
     * 获取缓存信息
     *
     * @param joinPoint
     * @param key       缓存key
     * @param value     缓存内容
     * @return
     */
    private String getCacheMsg(ProceedingJoinPoint joinPoint, String key, Object value) {
        return "key：" + key + "--->" + (value != null ? "not null, do not need to proceed method " + joinPoint.getSignature().getName() : "null, need to proceed method " + joinPoint.getSignature().getName());
    }


}
