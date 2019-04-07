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

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 缓存Key的生成器
 *
 * @author xuexiang
 * @since 2019/4/7 下午3:54
 */
public interface ICacheKeyCreator {
    /**
     * 根据签名，自动生成缓存的Key
     * @param joinPoint
     * @return
     */
    String getCacheKey(ProceedingJoinPoint joinPoint);
}
