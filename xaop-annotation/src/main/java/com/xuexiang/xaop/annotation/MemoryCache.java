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

package com.xuexiang.xaop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内存缓存代理注解，通过aop切片的方式在编译期间织入源代码中
 * <p>功能：缓存某方法的返回值，下次执行该方法时，直接从缓存里获取。</p>
 *
 * @author xuexiang
 * @since 2020/10/25 5:35 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MemoryCache {
    /**
     * 内存缓存的key，不设置的话会自动生成
     * 默认自动生成key的规则 ： 方法名(参数1名=参数1值|参数2名=参数2值|...)
     *
     * @return 内存缓存的key
     */
    String value() default "";

    /**
     * @return 对于String、数组和集合等，是否允许缓存为空, 默认为true
     */
    boolean enableEmpty() default true;
}
