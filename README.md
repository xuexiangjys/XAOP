# XAOP
一个简易的AOP(Android)应用框架。囊括了最实用的AOP应用。

[![xaop][xaopsvg]][xaop]  [![api][apisvg]][api]

-----------------------


[TOC]


-----------------------

## 关于我

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)

## 特点

* 支持快速点击切片`@SingleClick`，支持设置快速点击的时间间隔。

* 支持动态申请权限切片`@Permission`，支持自定义响应动作。

* 支持主线程切片`@MainThread`。

* 支持IO程切片`@IOThread`，支持多种线程池类型。

* 支持日志打印切片`@DebugLog`，支持自定义日志记录方式。

* 支持内存缓存切片`@MemoryCache`，支持设置缓存大小。

* 支持磁盘缓存切片`@DiskCache`，支持自定义磁盘缓存，缓存有效时间等。

## 1、演示（请star支持）

## 2、如何使用
目前支持主流开发工具AndroidStudio的使用，直接配置build.gradle，增加依赖即可.

### 2.1、Android Studio导入方法，添加Gradle依赖

1.先在项目根目录的 build.gradle 的 repositories 添加:
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.再在项目根目录的 build.gradle 的 dependencies 添加xmark插件：

```
buildscript {
    ···
    dependencies {
        ···
        classpath 'com.github.xuexiangjys.XAOP:xaop-plugin:1.0.0'
    }
}
```

3.在项目的 build.gradle 中增加依赖并引用xaop插件

```
apply plugin: 'com.xuexiang.xaop' //引用xaop插件

dependencies {
    ···
    implementation 'com.github.xuexiangjys.XAOP:xaop-runtime:1.0.0'  //添加依赖
}

```



## 联系方式

[![](https://img.shields.io/badge/点击一键加入QQ群-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=9922861ef85c19f1575aecea0e8680f60d9386080a97ed310c971ae074998887)

![](https://github.com/xuexiangjys/XPage/blob/master/img/qq_group.jpg)

[xaopsvg]: https://img.shields.io/badge/XAOP-v1.0.0-brightgreen.svg
[xaop]: https://github.com/xuexiangjys/XAOP
[apisvg]: https://img.shields.io/badge/API-14+-brightgreen.svg
[api]: https://android-arsenal.com/api?level=14