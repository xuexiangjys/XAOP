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

package com.xuexiang.xaop.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.logger.XLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     desc   :
 *     author : xuexiang
 *     time   : 2018/4/22 下午10:11
 * </pre>
 */
public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 根据分隔符将List转换为String
     *
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(final List<String> list, final String separator) {
        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 根据分隔符将List转换为String
     *
     * @param list
     * @return
     */
    public static String listToString(final List<String> list) {
        return listToString(list, ",");
    }


    public static String getClassName(Class<?> cls) {
        if (cls == null) {
            return "<UnKnow Class>";
        }
        if (cls.isAnonymousClass()) {
            return getClassName(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }

    public static String toString(Object object) {
        if (XLogger.getISerializer() != null) {
            return XLogger.getISerializer().toString(object);
        } else {
            return Strings.toString(object);
        }
    }

    /**
     * 获取方法的描述信息
     *
     * @param joinPoint
     * @return
     */
    public static String getMethodDescribeInfo(final ProceedingJoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = codeSignature.getDeclaringType(); //方法所在类
        String methodName = codeSignature.getName();    //方法名
        return Utils.getClassName(cls) + "->" + methodName;
    }

    /**
     * 获取简约的方法名
     *
     * @param joinPoint
     * @return
     */
    public static String getMethodName(final ProceedingJoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = codeSignature.getDeclaringType(); //方法所在类
        String methodName = codeSignature.getName();    //方法名
        return Utils.getClassName(cls) + "." + methodName;
    }

    public static <T> T checkNotNull(T t, String message) {
        if (t == null) {
            throw new NullPointerException(message);
        }
        return t;
    }

    /**
     * 关闭 IO
     *
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 应用程序缓存原理：
     * 1.当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，否则就调用getCacheDir()方法来获取缓存路径<br>
     * 2.前者是/sdcard/Android/data/<application package>/cache 这个路径<br>
     * 3.后者获取到的是 /data/data/<application package>/cache 这个路径<br>
     *
     * @param uniqueName 缓存目录
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (isSDCardEnable() && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable();
    }

    /**
     * 获取 App 版本码
     *
     * @param context
     * @return App 版本码
     */
    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * 获取缓存的key
     *
     * @param joinPoint
     * @return
     */
    @NonNull
    public static String getCacheKey(ProceedingJoinPoint joinPoint) {
        return XAOP.getICacheKeyCreator().getCacheKey(joinPoint);
    }

    /**
     * 方法是否有返回值
     *
     * @param signature
     * @return
     */
    public static boolean isHasReturnType(Signature signature) {
        return signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;
    }


    /**
     * 返回object是否为空
     *
     * @param obj The object.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return obj instanceof android.util.LongSparseArray
                    && ((android.util.LongSparseArray) obj).size() == 0;
        }
        return false;
    }

}
