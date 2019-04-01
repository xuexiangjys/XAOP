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

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.xuexiang.xaop.cache.converter.IDiskConverter;
import com.xuexiang.xaop.logger.XLogger;
import com.xuexiang.xaop.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * @author xuexiang
 * @since 2019/4/1 下午9:53
 */
public class GsonDiskConverter implements IDiskConverter {

    private Gson mGson;

    public GsonDiskConverter() {
        mGson = new Gson();
    }

    public GsonDiskConverter(Gson gson) {
        mGson = gson;
    }

    @Override
    public <T> T load(InputStream source, Type type) {
        T value = null;
        try {
            if (mGson == null) {
                mGson = new Gson();
            }
            value = mGson.fromJson(new InputStreamReader(source), type);
        } catch (JsonIOException e) {
            XLogger.e(e);
        } catch (JsonSyntaxException e) {
            XLogger.e(e);
        } finally {
            Utils.closeIO(source);
        }
        return value;
    }

    @Override
    public boolean writer(OutputStream sink, Object data) {
        try {
            String json = mGson.toJson(data);
            byte[] bytes = json.getBytes();
            sink.write(bytes, 0, bytes.length);
            sink.flush();
            return true;
        } catch (JsonIOException e) {
            XLogger.e(e);
        } catch (JsonSyntaxException e) {
            XLogger.e(e);
        } catch (IOException e) {
            XLogger.e(e);
        } finally {
            Utils.closeIO(sink);
        }
        return false;
    }
}
