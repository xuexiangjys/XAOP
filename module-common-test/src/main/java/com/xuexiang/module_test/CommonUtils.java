/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.module_test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.xuexiang.xaop.annotation.MemoryCache;

/**
 * 这里是演示的工具类
 *
 * @author xuexiang
 * @since 2021/10/6 10:51 AM
 */
public final class CommonUtils {

    private CommonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取上下文所在的宽度
     *
     * @param context 上下文
     * @param isReal  是否是真实的尺寸
     * @return 上下文所在的宽度
     */
    public static int getDisplayWidth(Context context, boolean isReal) {
        Point point = getDisplaySize(context, isReal);
        return point != null ? point.x : 0;
    }

    /**
     * 获取上下文所在的尺寸
     *
     * @param context 上下文
     * @param isReal  是否是真实的尺寸
     * @return 上下文所在的尺寸
     */
    @MemoryCache
    private static Point getDisplaySize(Context context, boolean isReal) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (windowManager == null) {
            return null;
        }
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (isReal) {
            display.getRealSize(point);
        } else {
            display.getSize(point);
        }
        return point;
    }


}
