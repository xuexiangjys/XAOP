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

package com.xuexiang.kotlintest

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import com.xuexiang.xutil.tip.ToastUtils


/**
 * 基础BindingActivity
 * @author xuexiang
 * @date 2018/3/14 下午2:24
 */
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: T

    /**
     * 布局的资源id
     *
     * @return
     */
    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        initArgs()
        bindViews()
        initListeners()
    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }


    /**
     * 初始化参数
     */
    protected fun initArgs() {

    }

    /**
     * 绑定ViewModel
     */
    protected abstract fun bindViews()

    /**
     * 初始化监听
     */
    protected fun initListeners() {

    }


    protected fun toast(msg: String) {
        ToastUtils.toast(msg)
    }
}