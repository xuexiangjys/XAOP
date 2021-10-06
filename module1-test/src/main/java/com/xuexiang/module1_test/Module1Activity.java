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

package com.xuexiang.module1_test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.module_test.CommonUtils;
import com.xuexiang.xaop.annotation.DebugLog;
import com.xuexiang.xaop.annotation.Intercept;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xutil.tip.ToastUtils;

public class Module1Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1);
        mTvDisplay = findViewById(R.id.tv_display);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_get) {
            int width = CommonUtils.getDisplayWidth(this, true);
            setText("屏幕宽度:" + width);
        } else if (id == R.id.btn_single_click) {
            handleOnClick(view);

        }
    }

    /**
     * 设置8秒内响应一次点击
     */
    @SingleClick(8000)
    @DebugLog(priority = Log.ERROR)
    public void handleOnClick(View v) {
        XLogger.e("点击响应！");
        ToastUtils.toast("点击响应！");
    }


    private void setText(String content) {
        mTvDisplay.setText(content);
    }
}
