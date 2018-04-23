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

package com.xuexiang.xaop.cache.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * <pre>
 *     desc   : 通用转换器接口
 *     author : xuexiang
 *     time   : 2018/4/23 下午10:11
 * </pre>
 */
public interface IDiskConverter {

    /**
     * 读取
     *
     * @param is 输入流
     * @param type  读取数据后要转换的数据类型
     * @return
     */
    <T> T load(InputStream is, Type type);

    /**
     * 写入
     *
     * @param os 输出流
     * @param data 保存的数据
     * @return
     */
    boolean writer(OutputStream os, Object data);
}
