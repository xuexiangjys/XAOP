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

import com.xuexiang.xaop.util.Utils;

import java.lang.reflect.Type;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * <pre>
 *     desc   : <p>描述：磁盘缓存的基类</p>
 *              1.所有缓存处理都继承该基类<br>
 *              2.增加了锁机制，防止频繁读取缓存造成的异常。<br>
 *              3.子类直接考虑具体的实现细节就可以了。<br>
 *              <p>
 *     author : xuexiang
 *     time   : 2018/4/23 下午9:59
 * </pre>
 */
public abstract class BaseDiskCache implements ICache {

    private final ReadWriteLock mLock = new ReentrantReadWriteLock();

    /**
     * 读取缓存
     *
     * @param key       缓存key
     * @param existTime 缓存时间
     */
    @Override
    public final <T> T load(Type type, String key, long existTime) {
        //1.先检查key
        Utils.checkNotNull(key, "key == null");

        //2.判断key是否存在,key不存在去读缓存没意义
        if (!containsKey(key)) {
            return null;
        }

        //3.判断是否过期，过期自动清理
        if (isExpiry(key, existTime)) {
            remove(key);
            return null;
        }

        //4.开始真正的读取缓存
        mLock.readLock().lock();
        try {
            // 读取缓存
            return doLoad(type, key);
        } finally {
            mLock.readLock().unlock();
        }
    }

    /**
     * 保存缓存
     *
     * @param key   缓存key
     * @param value 缓存内容
     * @return
     */
    @Override
    public <T> boolean save(String key, T value) {
        //1.先检查key
        Utils.checkNotNull(key, "key == null");

        //2.如果要保存的值为空,则删除
        if (value == null) {
            return remove(key);
        }

        //3.写入缓存
        boolean status = false;
        mLock.writeLock().lock();
        try {
            status = doSave(key, value);
        } finally {
            mLock.writeLock().unlock();
        }
        return status;
    }

    /**
     * 删除缓存
     */
    @Override
    public final boolean remove(String key) {
        mLock.writeLock().lock();
        try {
            return doRemove(key);
        } finally {
            mLock.writeLock().unlock();
        }
    }

    /**
     * 清空缓存
     */
    @Override
    public final boolean clear() {
        mLock.writeLock().lock();
        try {
            return doClear();
        } finally {
            mLock.writeLock().unlock();
        }
    }

    /**
     * 是否包含 加final 是让子类不能被重写，只能使用doContainsKey<br>
     * 这里加了锁处理，操作安全。<br>
     *
     * @param key 缓存key
     * @return 是否有缓存
     */
    @Override
    public final boolean containsKey(String key) {
        mLock.readLock().lock();
        try {
            return doContainsKey(key);
        } finally {
            mLock.readLock().unlock();
        }
    }

    /**
     * 是否包含  采用protected修饰符  被子类修改
     */
    protected abstract boolean doContainsKey(String key);

    /**
     * 是否过期
     */
    protected abstract boolean isExpiry(String key, long existTime);

    /**
     * 读取缓存
     */
    protected abstract <T> T doLoad(Type type, String key);

    /**
     * 保存
     */
    protected abstract <T> boolean doSave(String key, T value);

    /**
     * 删除缓存
     */
    protected abstract boolean doRemove(String key);

    /**
     * 清空缓存
     */
    protected abstract boolean doClear();
}
