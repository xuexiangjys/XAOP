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

import java.io.Serializable;
import java.util.Arrays;

/**
 * 【注意，磁盘缓存如果使用默认的SerializableDiskConverter，对象一定要实现Serializable接口】
 *
 * @author xuexiang
 * @since 2019/4/1 下午9:39
 */
public class LoginInfo implements Serializable {

    private String LoginName;

    private int Age;

    private boolean IsBoy;

    private int[] score;

    public LoginInfo random() {
        LoginName = "xuexiang" + (int) (Math.random() * 1000 + 50);
        Age = (int) (Math.random() * 10 + 20);
        IsBoy = (Age % 2 == 0);
        score = new int[]{(int) (Math.random() * 100 + 50), (int) (Math.random() * 100 + 50)};
        return this;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "LoginName='" + LoginName + '\'' +
                ", Age=" + Age +
                ", IsBoy=" + IsBoy +
                ", score=" + Arrays.toString(score) +
                '}';
    }
}
