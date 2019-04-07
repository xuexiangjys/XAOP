/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.xaop.cache.key;

import com.xuexiang.xaop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

/**
 * <p>key规则 ： 方法名(参数1名=参数1值|参数2名=参数2值|...)</p>
 *
 * @author xuexiang
 * @since 2019/4/7 下午3:58
 */
public class DefaultCacheKeyCreator implements ICacheKeyCreator {
    /**
     * 根据签名，自动生成缓存的Key
     *
     * @param joinPoint
     * @return
     */
    @Override
    public String getCacheKey(ProceedingJoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames(); //方法参数名集合
        Object[] parameterValues = joinPoint.getArgs(); //方法参数集合

        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                keyBuilder.append("|");
            }
            keyBuilder.append(parameterNames[i]).append('=');
            keyBuilder.append(Utils.toString(parameterValues[i]));
        }
        keyBuilder.append(')');
        return keyBuilder.toString();
    }
}
