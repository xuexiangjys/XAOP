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

package com.xuexiang.xaop.checker;

/**
 * <pre>
 *     desc   : 异常的处理
 *     author : xuexiang
 *     time   : 2018/5/14 下午11:04
 * </pre>
 */
public interface IThrowableHandler {

    /**
     * 处理异常
     * @param flag 异常的标志
     * @param throwable 捕获到的异常
     * @return
     */
    Object handleThrowable(String flag, Throwable throwable);
}
