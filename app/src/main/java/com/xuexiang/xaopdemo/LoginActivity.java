/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xutil.tip.ToastUtils;

/**
 * @author xuexiang
 * @since 2019/3/5 下午9:45
 */
public class LoginActivity extends AppCompatActivity {

    public static boolean sIsLogined = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    @SingleClick
    public void handleOnClick(View v) {
        ToastUtils.toast("登录成功！");
        sIsLogined = true;
        finish();
    }
}
