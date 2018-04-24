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

package com.xuexiang.xaop.checker;

import org.aspectj.lang.JoinPoint;

/**
 * <pre>
 *     desc   : 自定义拦截切片的拦截器实现接口
 *     author : xuexiang
 *     time   : 2018/4/24 下午10:11
 * </pre>
 */
public interface Interceptor {

    /**
     * 执行拦截
     * @param type 拦截的类型
     * @param joinPoint 切片切点
     * @return {@code true}: 拦截切片的执行 <br>{@code false}: 不拦截切片的执行
     * @throws Throwable
     */
    boolean intercept(int type, JoinPoint joinPoint) throws Throwable;

}
