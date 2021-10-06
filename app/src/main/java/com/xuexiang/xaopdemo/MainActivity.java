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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.module1_test.Module1Activity;
import com.xuexiang.xaop.annotation.DebugLog;
import com.xuexiang.xaop.annotation.DiskCache;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.Intercept;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.Safe;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.cache.XDiskCache;
import com.xuexiang.xaop.cache.XMemoryCache;
import com.xuexiang.xaop.consts.PermissionConsts;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xutil.system.AppExecutors;
import com.xuexiang.xutil.tip.ToastUtils;

import static com.xuexiang.xaopdemo.App.INTERCEPT_LOGIN;
import static com.xuexiang.xaopdemo.App.TRY_CATCH_KEY;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTvHello;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvHello = findViewById(R.id.tv_hello);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_single_click:
                handleOnClick(v);
                break;
            case R.id.btn_request_permission:
                handleRequestPermission(v);
                break;
            case R.id.btn_main_thread:
                AppExecutors.get().networkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        doInMainThread(v);
                    }
                });
                break;
            case R.id.btn_io_thread:
                doInIOThread(v);
                break;
            case R.id.btn_memory_cache:
                Log.e("xuexiang", "@MemoryCache getMemoryCacheLoginInfo:" + getMemoryCacheLoginInfo());
                break;
            case R.id.btn_disk_cache:
                Log.e("xuexiang", "@DiskCache getDiskCacheLoginInfo:" + getDiskCacheLoginInfo());
//                testDiskCache1();
//                XLogger.e("testDiskCache2:" + testDiskCache2());
//                testDiskCache3();
//                testDiskCache4((int) (Math.random() * 100), "1234");
//                testDiskCache5();
                break;
            case R.id.btn_clear_cache:
                XMemoryCache.getInstance().clear();
                XDiskCache.getInstance().clear();
                break;
            case R.id.btn_try_catch:
                int result = getNumber();
                mTvHello.setText("结果为:" + result);
                break;
            case R.id.btn_intercept_login:
                doSomeThing();
                break;
            case R.id.btn_lambda:
                AppExecutors.get().networkIO().execute(() -> doInMainThread(v));
                break;
            case R.id.btn_module1:
                startActivity(new Intent(this, Module1Activity.class));
                break;
            default:
                break;
        }
    }


    @SingleClick
    @Permission({PermissionConsts.CALENDAR, PermissionConsts.CAMERA, PermissionConsts.LOCATION})
    private void handleRequestPermission(View v) {
        ToastUtils.toast("权限申请通过！");
    }

    /**
     * 设置5秒内响应一次点击
     */
    @SingleClick(5000)
    @DebugLog(priority = Log.ERROR)
    @Intercept(3)
    public void handleOnClick(View v) {
        XLogger.e("点击响应！");
        ToastUtils.toast("点击响应！");
        hello("xuexiangjys", "666666");
    }


    @DebugLog(priority = Log.ERROR)
    @Intercept({1,2,3})
//    @MemoryCache
    @DiskCache
    private String hello(String name, String cardId) {
        return "hello, " + name + "! Your CardId is " + cardId + ".";
    }


    @MainThread
    private void doInMainThread(View v) {
        mTvHello.setText("工作在主线程");
    }

    @IOThread(ThreadType.Single)
    @MemoryCache("12345")
//    @DiskCache
    private String doInIOThread(View v) {
        return "io线程名:" + Thread.currentThread().getName();
    }

    @MemoryCache
    private LoginInfo getMemoryCacheLoginInfo() {
        LoginInfo loginInfo = new LoginInfo().random();
        ToastUtils.toast("执行了getMemoryCacheLoginInfo():" + loginInfo);
        return loginInfo;
    }

    @DiskCache("LoginInfo")
    private LoginInfo getDiskCacheLoginInfo() {
        LoginInfo loginInfo = new LoginInfo().random();
        ToastUtils.toast("执行了getDiskCacheLoginInfo():" + loginInfo);
        return loginInfo;
    }

    @DiskCache
    private int testDiskCache1() {
        return 2;
    }

    @DiskCache
    private String testDiskCache2() {
        count ++;
        if (count % 3 == 0) {
            return "123";
        } else {
            return "";
        }
    }

    @DiskCache
    private int[] testDiskCache3() {
        return new int[] {5,6,7};
    }

    @DiskCache
    private String[] testDiskCache4(int num, String name) {
        return new String[]{"234", "345"};
    }

    @DiskCache
    private int[] testDiskCache5() {
        return null;
    }


    @Intercept(INTERCEPT_LOGIN)
    public void doSomeThing() {
        ToastUtils.toast("已登陆过啦～～");
    }

    @Safe(TRY_CATCH_KEY)
    private int getNumber() {
        return 100 / 0;
    }

}
