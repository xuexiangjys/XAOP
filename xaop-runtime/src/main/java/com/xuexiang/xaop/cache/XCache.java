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

import android.content.Context;
import android.os.StatFs;

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.cache.converter.IDiskConverter;
import com.xuexiang.xaop.cache.converter.SerializableDiskConverter;
import com.xuexiang.xaop.cache.core.CacheCore;
import com.xuexiang.xaop.cache.core.LruDiskCache;
import com.xuexiang.xaop.cache.core.LruMemoryCache;
import com.xuexiang.xaop.util.Utils;

import java.io.File;
import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 缓存管理
 *     author : xuexiang
 *     time   : 2018/4/23 下午10:19
 * </pre>
 */
public class XCache {
    /**
     * 缓存的核心管理类
     */
    private CacheCore mCacheCore;
    /**
     * 缓存的时间 单位:秒
     */
    private long mCacheTime;

    public static XCache newInstance() {
        return new XCache();
    }

    public XCache() {
        this(new Builder().builder());
    }

    public XCache(Builder builder) {
        init(builder);
    }

    /**
     * 初始化缓存
     *
     * @param builder
     * @return
     */
    public XCache init(Builder builder) {
        if (builder.isDiskCache) {
            mCacheTime = builder.cacheTime;
            mCacheCore = new CacheCore(new LruDiskCache(builder.diskConverter, builder.diskDir, builder.appVersion, builder.diskMaxSize));
        } else {
            mCacheCore = new CacheCore(new LruMemoryCache(builder.memoryMaxSize));
        }
        return this;
    }

    /**
     * 读取缓存
     *
     * @param key 缓存key
     */
    public <T> T load(final String key) {
        return mCacheCore.load(null, key, mCacheTime);
    }

    /**
     * 读取缓存
     *
     * @param key  缓存key
     * @param time 保存时间
     */
    public <T> T load(final String key, final long time) {
        return mCacheCore.load(null, key, time);
    }

    /**
     * 读取缓存
     *
     * @param type 保存的类型
     * @param key  缓存key
     * @param time 保存时间
     */
    public <T> T load(final Type type, final String key, final long time) {
        return mCacheCore.load(type, key, time);
    }

    /**
     * 保存缓存
     *
     * @param key   缓存key
     * @param value 缓存Value
     */
    public <T> boolean save(final String key, final T value) {
        return mCacheCore.save(key, value);
    }

    /**
     * 是否包含
     */
    public boolean containsKey(final String key) {
        return mCacheCore.containsKey(key);
    }

    /**
     * 删除缓存
     */
    public boolean remove(final String key) {
        return mCacheCore.remove(key);
    }

    /**
     * 清空缓存
     */
    public boolean clear() {
        return mCacheCore.clear();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * 构造器
     */
    public static final class Builder {
        private static final int MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024; // 5MB
        private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
        private boolean isDiskCache;
        //构建内存缓存需要的属性
        private int memoryMaxSize;
        //构建磁盘缓存需要的属性
        private Context context;
        private long cacheTime;
        private IDiskConverter diskConverter;
        private File diskDir;
        private int appVersion;
        private long diskMaxSize;

        public Builder() {
            this(XAOP.getContext());
        }

        public Builder(Context context) {
            this.isDiskCache = false;
            this.context = context;
            this.diskConverter = new SerializableDiskConverter();
            this.cacheTime = LruDiskCache.CACHE_NEVER_EXPIRE;
            this.appVersion = Utils.getAppVersionCode(context);
        }

        /**
         * 设置是否是磁盘缓存
         *
         * @param isDiskCache
         * @return
         */
        public Builder isDiskCache(boolean isDiskCache) {
            this.isDiskCache = isDiskCache;
            return this;
        }

        /**
         * 设置内存缓存的最大数量
         *
         * @param memoryMaxSize
         * @return
         */
        public Builder memoryMaxSize(int memoryMaxSize) {
            this.memoryMaxSize = memoryMaxSize;
            return this;
        }

        /**
         * 设置磁盘缓存的有效期（单位:s）
         *
         * @param cacheTime
         * @return
         */
        public Builder cacheTime(long cacheTime) {
            this.cacheTime = cacheTime;
            return this;
        }

        /**
         * 不设置，默认为App的VersionCode
         */
        public Builder appVersion(int appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        /**
         * 默认磁盘缓存目录路径
         *
         * @param directory
         * @return
         */
        public Builder diskDir(File directory) {
            this.diskDir = directory;
            return this;
        }


        /**
         * 设置磁盘缓存的转化器，默认是SerializableDiskConverter
         *
         * @param converter
         * @return
         */
        public Builder diskConverter(IDiskConverter converter) {
            this.diskConverter = converter;
            return this;
        }

        /**
         * 设置磁盘的最大缓存大小， 默认为50MB
         */
        public Builder diskMax(long maxSize) {
            this.diskMaxSize = maxSize;
            return this;
        }

        /**
         * 构建属性
         * @return
         */
        public Builder builder() {
            if (isDiskCache) { //初始化磁盘缓存的属性
                if (this.diskConverter == null) {
                    this.diskConverter = new SerializableDiskConverter();
                }
                if (this.diskDir == null) {
                    this.diskDir = Utils.getDiskCacheDir(this.context, "data-cache");
                }
                Utils.checkNotNull(this.diskDir, "diskDir==null");
                if (!this.diskDir.exists()) {
                    this.diskDir.mkdirs();
                }
                if (diskMaxSize <= 0) {
                    diskMaxSize = calculateDiskCacheSize(diskDir);
                }
                cacheTime = Math.max(LruDiskCache.CACHE_NEVER_EXPIRE, this.cacheTime);

                appVersion = Math.max(Utils.getAppVersionCode(this.context), this.appVersion);
            } else { //初始化内存缓存的属性
                if (memoryMaxSize <= 0) {
                    memoryMaxSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;
                }
            }
            return this;
        }

        public XCache build() {
            return new XCache(builder());
        }

        private static long calculateDiskCacheSize(File dir) {
            long size = 0;
            try {
                StatFs statFs = new StatFs(dir.getAbsolutePath());
                long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
                size = available / 50;
            } catch (IllegalArgumentException ignored) {
            }
            return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE);
        }
    }
}
