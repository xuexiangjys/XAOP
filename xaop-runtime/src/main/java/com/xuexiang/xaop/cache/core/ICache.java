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

package com.xuexiang.xaop.cache.core;

import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 缓存需要实现的接口
 *     author : xuexiang
 *     time   : 2018/4/23 下午9:27
 * </pre>
 */
public interface ICache {

    /**
     * 读取缓存
     * @param type 对象的类型
     * @param key
     * @param time 有效期
     * @return
     */
    <T> T load(Type type, String key, long time);

    /**
     * 保存缓存
     * @param key
     * @param value
     * @return
     */
    <T> boolean save(String key, T value);

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    boolean containsKey(String key);

    /**
     * 删除缓存
     *
     * @param key
     */
    boolean remove(String key);

    /**
     * 清空缓存
     */
    boolean clear();
}
