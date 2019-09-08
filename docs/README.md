# XAOP
一个轻量级的AOP(Android)应用框架。囊括了最实用的AOP应用。

[![](https://jitpack.io/v/xuexiangjys/XAOP.svg)](https://jitpack.io/#xuexiangjys/XAOP)
[![api](https://img.shields.io/badge/API-14+-brightgreen.svg)](https://android-arsenal.com/api?level=14)
[![Issue](https://img.shields.io/github/issues/xuexiangjys/XAOP.svg)](https://github.com/xuexiangjys/XAOP/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XAOP.svg)](https://github.com/xuexiangjys/XAOP)

## 关于我

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)   [![简书](https://img.shields.io/badge/简书-xuexiangjys-red.svg)](https://www.jianshu.com/u/6bf605575337)   [![掘金](https://img.shields.io/badge/掘金-xuexiangjys-brightgreen.svg)](https://juejin.im/user/598feef55188257d592e56ed)   [![知乎](https://img.shields.io/badge/知乎-xuexiangjys-violet.svg)](https://www.zhihu.com/people/xuexiangjys)

## 特点

* 支持快速点击切片`@SingleClick`，支持设置快速点击的时间间隔。

* 支持动态申请权限切片`@Permission`，支持自定义响应动作。

* 支持主线程切片`@MainThread`。

* 支持IO线程切片`@IOThread`，支持多种线程池类型。

* 支持日志打印切片`@DebugLog`，支持自定义日志记录方式。

* 支持内存缓存切片`@MemoryCache`，支持设置缓存大小。

* 支持磁盘缓存切片`@DiskCache`，支持自定义磁盘缓存，缓存有效时间等。

* 支持自动捕获异常的拦截切片`@Safe`，支持设置自定义异常处理者。

* 支持自定义拦截切片`@Intercept`，支持自定义切片拦截。

## 如何使用

1.先在项目根目录的 build.gradle 的 repositories 添加:
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.再在项目根目录的 build.gradle 的 dependencies 添加xaop插件：

```
buildscript {
    ···
    dependencies {
        ···
        classpath 'com.github.xuexiangjys.XAOP:xaop-plugin:1.0.5'
    }
}
```

3.在项目的 build.gradle 中增加依赖并引用xaop插件

```
apply plugin: 'com.xuexiang.xaop' //引用xaop插件

dependencies {
    ···
    implementation 'com.github.xuexiangjys.XAOP:xaop-runtime:1.0.5'  //添加依赖
    //如果你升级到androidx，请使用下面依赖
    implementation 'com.github.xuexiangjys.XAOP:xaop-runtime:1.0.5x'
}

```
4.在Application中进行初始化

```

XAOP.init(this); //初始化插件
XAOP.debug(true); //日志打印切片开启
XAOP.setPriority(Log.INFO); //设置日志打印的等级,默认为0

//设置动态申请权限切片 申请权限被拒绝的事件响应监听
XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
    @Override
    public void onDenied(List<String> permissionsDenied) {
        ToastUtil.get().toast("权限申请被拒绝:" + Utils.listToString(permissionsDenied));
    }

});

//设置自定义拦截切片的处理拦截器
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

```

## 联系方式

[![](https://img.shields.io/badge/点击一键加入QQ群-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=9922861ef85c19f1575aecea0e8680f60d9386080a97ed310c971ae074998887)

