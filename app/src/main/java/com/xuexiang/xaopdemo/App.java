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

package com.xuexiang.xaopdemo;

import android.app.Application;

import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.checker.IThrowableHandler;
import com.xuexiang.xaop.checker.Interceptor;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.PermissionUtils;
import com.xuexiang.xaop.util.Utils;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.tip.ToastUtils;

import org.aspectj.lang.JoinPoint;

import java.util.List;

/**
 * <pre>
 *     desc   :
 *     author : xuexiang
 *     time   : 2018/4/22 下午7:13
 * </pre>
 */
public class App extends Application {

    public static final String TRY_CATCH_KEY = "getNumber";
    @Override
    public void onCreate() {
        super.onCreate();

        XUtil.init(this);

        XAOP.init(this);
        XAOP.debug(true);
//        XAOP.setPriority(Log.INFO);
        XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
            /**
             * 权限申请被拒绝
             *
             * @param permissionsDenied
             */
            @Override
            public void onDenied(List<String> permissionsDenied) {
                ToastUtils.toast("权限申请被拒绝:" + Utils.listToString(permissionsDenied));
            }

        });

        XAOP.setInterceptor(new Interceptor() {
            @Override
            public boolean intercept(int type, JoinPoint joinPoint) throws Throwable {
                XLogger.d("正在进行拦截，拦截类型:" + type);
                switch(type) {
                    case 1:
                        //做你想要的拦截
                        break;
                    case 2:
                        return true; //return true，直接拦截切片的执行
                    default:
                        break;
                }
                return false;
            }
        });

        XAOP.setIThrowableHandler(new IThrowableHandler() {
            @Override
            public Object handleThrowable(String flag, Throwable throwable) {
                XLogger.d("捕获到异常，异常的flag:" + flag);
                if (flag.equals(TRY_CATCH_KEY)) {
                    return 100;
                }
                return null;
            }
        });
    }
}
