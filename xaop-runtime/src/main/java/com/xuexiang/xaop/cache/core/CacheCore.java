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

import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.MD5Utils;
import com.xuexiang.xaop.util.Utils;

import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 缓存核心管理类
 *              <p>1.采用LruDiskCache</p>
 *              <p>2.对Key进行MD5加密</p>
 *     author : xuexiang
 *     time   : 2018/4/23 下午9:45
 * </pre>
 */
public class CacheCore implements ICache {

    private ICache mCache;

    public CacheCore(ICache cache) {
        mCache = Utils.checkNotNull(cache, "ICache == null");
    }

    /**
     * 设置缓存实现接口
     * @param icache
     * @return
     */
    public CacheCore setICache(ICache icache) {
        mCache = icache;
        return this;
    }

    /**
     * 读取
     */
    @Override
    public <T> T load(Type type, String key, long time) {
        if (mCache != null) {
            String cacheKey = MD5Utils.encode(key);
            XLogger.d("loadCache  key=" + cacheKey);
            return mCache.load(type, cacheKey, time);
        }
        return null;
    }

    /**
     * 保存
     */
    @Override
    public <T> boolean save(String key, T value) {
        if (mCache != null) {
            String cacheKey = MD5Utils.encode(key);
            XLogger.d("saveCache  key=" + cacheKey);
            return mCache.save(cacheKey, value);
        }
        return false;
    }

    /**
     * 是否包含
     *
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        if (mCache != null) {
            String cacheKey = MD5Utils.encode(key);
            XLogger.d("containsCache  key=" + cacheKey);
            return mCache.containsKey(cacheKey);
        }
        return false;
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    @Override
    public boolean remove(String key) {
        String cacheKey = MD5Utils.encode(key);
        XLogger.d("removeCache  key=" + cacheKey);
        return mCache == null || mCache.remove(cacheKey);
    }

    /**
     * 清空缓存
     */
    @Override
    public boolean clear() {
        return mCache != null && mCache.clear();
    }

}
