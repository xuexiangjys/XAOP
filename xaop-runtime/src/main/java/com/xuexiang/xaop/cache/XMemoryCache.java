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

package com.xuexiang.xaop.cache;

/**
 * <pre>
 *     desc   : 内存缓存管理器
 *     author : xuexiang
 *     time   : 2018/4/23 下午11:59
 * </pre>
 */
public final class XMemoryCache {

    private static XMemoryCache sInstance;

    private XCache mXCache;

    private XMemoryCache() {
        mXCache = XCache.newInstance();
    }

    /**
     * 获取内存缓存管理实例
     *
     * @return
     */
    public static XMemoryCache getInstance() {
        if (sInstance == null) {
            synchronized (XMemoryCache.class) {
                if (sInstance == null) {
                    sInstance = new XMemoryCache();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化内存缓存
     * @param memoryMaxSize 内存缓存的最大数量
     * @return
     */
    public XMemoryCache init(int memoryMaxSize) {
        mXCache.init(XCache.newBuilder().memoryMaxSize(memoryMaxSize));
        return this;
    }

    /**
     * 读取缓存
     *
     * @param key 缓存key
     */
    public <T> T load(final String key) {
        return mXCache.load(key);
    }

    /**
     * 保存缓存
     *
     * @param key   缓存key
     * @param value 缓存Value
     */
    public <T> boolean save(final String key, final T value) {
        return mXCache.save(key, value);
    }

    /**
     * 是否包含
     */
    public boolean containsKey(final String key) {
        return mXCache.containsKey(key);
    }

    /**
     * 删除缓存
     */
    public boolean remove(final String key) {
        return mXCache.remove(key);
    }

    /**
     * 清空缓存
     */
    public boolean clear() {
        return mXCache.clear();
    }

}
