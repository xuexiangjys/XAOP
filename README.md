# XAOP

[![](https://jitpack.io/v/xuexiangjys/XAOP.svg)](https://jitpack.io/#xuexiangjys/XAOP)
[![api](https://img.shields.io/badge/API-14+-brightgreen.svg)](https://android-arsenal.com/api?level=14)
[![Issue](https://img.shields.io/github/issues/xuexiangjys/XAOP.svg)](https://github.com/xuexiangjys/XAOP/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XAOP.svg)](https://github.com/xuexiangjys/XAOP)

一个轻量级的AOP(Android)应用框架, 囊括了最实用的AOP应用。心动不如行动，还不赶紧点击[使用说明文档](https://github.com/xuexiangjys/XAOP/wiki)，体验一下吧！

## 关于我

| 公众号   | 掘金     |  知乎    |  CSDN   |   简书   |   思否  |   哔哩哔哩  |   今日头条
|---------|---------|--------- |---------|---------|---------|---------|---------|
| [我的Android开源之旅](https://t.1yb.co/Irse)  |  [点我](https://juejin.im/user/598feef55188257d592e56ed/posts)    |   [点我](https://www.zhihu.com/people/xuexiangjys/posts)       |   [点我](https://xuexiangjys.blog.csdn.net/)  |   [点我](https://www.jianshu.com/u/6bf605575337)  |   [点我](https://segmentfault.com/u/xuexiangjys)  |   [点我](https://space.bilibili.com/483850585)  |   [点我](https://img.rruu.net/image/5ff34ff7b02dd)

## X系列库快速集成

为了方便大家快速集成X系列框架库，我提供了一个空壳模版供大家参考使用: https://github.com/xuexiangjys/TemplateAppProject

---

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

* 兼容Kotlin语法。

* 支持androidx。

## Star趋势图

[![Stargazers over time](https://starchart.cc/xuexiangjys/XAOP.svg)](https://starchart.cc/xuexiangjys/XAOP)

---

## 1、演示（请star支持）

![aop.gif](./img/aop.gif)

## 2、如何使用
目前支持主流开发工具AndroidStudio的使用，直接配置build.gradle，增加依赖即可.

### 2.1、Android Studio导入方法，添加Gradle依赖

1.先在项目根目录的 `build.gradle` 的 repositories 添加:
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.再在项目根目录的 `build.gradle` 的 dependencies 添加xaop插件：

```
buildscript {
    ···
    dependencies {
        ···
        classpath 'com.github.xuexiangjys.XAOP:xaop-plugin:1.1.0'
    }
}
```

3.在应用项目(一般是app)的 `build.gradle` 中增加依赖并引用xaop插件

```
apply plugin: 'com.xuexiang.xaop' //引用xaop插件

dependencies {
    ···
    //如果是androidx项目，使用1.1.0版本及以上
    implementation 'com.github.xuexiangjys.XAOP:xaop-runtime:1.1.0'
    //如果是support项目，请使用1.0.5版本
    implementation 'com.github.xuexiangjys.XAOP:xaop-runtime:1.0.5'
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
        //申请权限被拒绝的处理
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

//设置自动捕获异常的处理者
XAOP.setIThrowableHandler(new IThrowableHandler() {
    @Override
    public Object handleThrowable(String flag, Throwable throwable) {
        XLogger.d("捕获到异常，异常的flag:" + flag);
        if (flag.equals(TRY_CATCH_KEY)) {
            return 100;
        }
        return null;
    }
});

```

### 2.2、兼容Kotlin语法配置

1.在项目根目录的 `build.gradle` 的 dependencies 添加 [aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx) 插件：

```
buildscript {
    ···
    dependencies {
        ···
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10'
    }
}
```

2.在应用项目(一般是app)的 `build.gradle` 中增加依赖并引用 [aspectjx](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx) 插件

```
apply plugin: 'android-aspectjx' //引用aspectjx插件

aspectjx {
    include '项目的applicationId'
}

```

详细使用可参见[kotlin-test](https://github.com/xuexiangjys/XAOP/blob/master/kotlin-test/build.gradle)项目进行使用.

## 3、切片的使用

### 3.1、快速点击切片使用

1.使用`@SingleClick`标注点击的方法。注意点击的方法中一定要有点击控件View作为方法参数，否则将不起作用。

2.可以设置快速点击的时间间隔，单位:ms。不设置的话默认是1000ms。

```
@SingleClick(5000)
public void handleOnClick(View v) {
    XLogger.e("点击响应！");
    ToastUtil.get().toast("点击响应！");
    hello("xuexiangjys", "666666");
}

```

### 3.2、动态申请权限切片使用

1.使用`@Permission`标注需要申请权限执行的方法。可设置申请一个或多个权限。

2.使用`@Permission`标注的方法，在执行时会自动判断是否需要申请权限。

```
@SingleClick
@Permission({PermissionConsts.CALENDAR, PermissionConsts.CAMERA, PermissionConsts.LOCATION})
private void handleRequestPermission(View v) {

}
```

### 3.3、主线程切片使用

1.使用`@MainThread`标注需要在主线程中执行的方法。

2.使用`@MainThread`标注的方法，在执行时会自动切换至主线程。

```
@MainThread
private void doInMainThread(View v) {
    mTvHello.setText("工作在主线程");
}
```

### 3.4、IO线程切片使用

1.使用`@IOThread`标注需要在io线程中执行的方法。可设置线程池的类型`ThreadType`，不设置的话默认是Fixed类型。

线程池的类型如下:

* Single:单线程池
* Fixed:多线程池
* Disk:磁盘读写线程池(本质上是单线程池）
* Network:网络请求线程池(本质上是多线程池）

2.使用`@IOThread`标注的方法，在执行时会自动切换至指定类型的io线程。

```
@IOThread(ThreadType.Single)
private String doInIOThread(View v) {
    return "io线程名:" + Thread.currentThread().getName();
}
```

### 3.5、日志打印切片使用

1.使用`@DebugLog`标注需要打印的方法和类。可设置打印的优先级，不设置的话默认优先级为0。注意：如果打印的优先级比`XAOP.setPriority`设置的优先级小的话，将不会进行打印。

2.使用`@DebugLog`标注的类和方法在执行的过程中，方法名、参数、执行的时间以及结果都将会被打印。

3.可调用`XAOP.setISerializer`设置打印时序列化参数对象的序列化器。

4.可调用`XAOP.setLogger`设置打印的实现接口。默认提供的是突破4000限制的logcat日志打印。

```
@DebugLog(priority = Log.ERROR)
private String hello(String name, String cardId) {
    return "hello, " + name + "! Your CardId is " + cardId + ".";
}
```

### 3.6、内存缓存切片使用

1.使用`@MemoryCache`标注需要内存缓存的方法。可设置缓存的key，不设置的话默认key为`方法名(参数1名=参数1值|参数2名=参数2值|...)`,当然你也可以修改key的自动生成规则，你只需要调用`XAOP.setICacheKeyCreator`即可。

2.标注的方法一定要有返回值，否则内存缓存切片将不起作用。

3.使用`@MemoryCache`标注的方法，可自动实现缓存策略。默认使用的内存缓存是`LruCache`。

4.可调用`XAOP.initMemoryCache`设置内存缓存的最大数量。默认是`Runtime.getRuntime().maxMemory() / 1024) / 8`

```
@MemoryCache
private String hello(String name, String cardId) {
    return "hello, " + name + "! Your CardId is " + cardId + ".";
}

```

### 3.7、磁盘缓存切片使用

1.使用`@DiskCache`标注需要磁盘缓存的方法。可设置缓存的key，不设置的话默认key为`方法名(参数1名=参数1值|参数2名=参数2值|...)`,当然你也可以修改key的自动生成规则，你只需要调用`XAOP.setICacheKeyCreator`即可。

2.可设置磁盘缓存的有效期，单位:s。不设置的话默认永久有效。

3.标注的方法一定要有返回值，否则磁盘缓存切片将不起作用。

4.使用`@DiskCache`标注的方法，可自动实现缓存策略。默认使用的磁盘缓存是JakeWharton的`DiskLruCache`。

5.可调用`XAOP.initDiskCache`设置磁盘缓存的属性,包括磁盘序列化器`IDiskConverter`，磁盘缓存的根目录，磁盘缓存的最大空间等。

```
@DiskCache
private String hello(String name, String cardId) {
    return "hello, " + name + "! Your CardId is " + cardId + ".";
}

```

### 3.8、自动捕获异常切片使用

1.使用`@Safe`标注需要进行异常捕获的方法。可设置一个异常捕获的标志Flag，默认的Flag为当前`类名.方法名`。

2.调用`XAOP.setIThrowableHandler`设置捕获异常的自定义处理者，可实现对异常的弥补处理。如果不设置的话，将只打印异常的堆栈信息。

3.使用`@Safe`标注的方法,可自动进行异常捕获，并统一进行异常处理，保证方法平稳执行。

```
@Safe(TRY_CATCH_KEY)
private int getNumber() {
    return 100 / 0;
}
```

### 3.9、自定义拦截切片使用

1.使用`@Intercept`标注需要进行拦截的方法和类。可设置申请一个或多个拦截类型。

2.如果不调用`XAOP.setInterceptor`设置切片拦截的拦截器的话，自定义拦截切片将不起作用。

3.使用`@Intercept`标注的类和方法，在执行时将自动调用`XAOP`设置的拦截器进行拦截处理。如果拦截器处理返回true的话，该类或方法的执行将被拦截，不执行。

4.使用`@Intercept`可以灵活地进行切片拦截。比如用户登录权限等。

```
@SingleClick(5000)
@DebugLog(priority = Log.ERROR)
@Intercept(3)
public void handleOnClick(View v) {
    XLogger.e("点击响应！");
    ToastUtil.get().toast("点击响应！");
    hello("xuexiangjys", "666666");
}

@DebugLog(priority = Log.ERROR)
@Intercept({1,2,3})
private String hello(String name, String cardId) {
    return "hello, " + name + "! Your CardId is " + cardId + ".";
}
```

【注意】：当有多个切片注解修饰时，一般是从上至下依次顺序执行。

## 4、混淆配置

```
-keep @com.xuexiang.xaop.annotation.* class * {*;}
-keep @org.aspectj.lang.annotation.* class * {*;}
-keep class * {
    @com.xuexiang.xaop.annotation.* <fields>;
    @org.aspectj.lang.annotation.* <fields>;
}
-keepclassmembers class * {
    @com.xuexiang.xaop.annotation.* <methods>;
    @org.aspectj.lang.annotation.* <methods>;
}
```

---

## 如果觉得项目还不错，可以考虑打赏一波

> 你的打赏是我维护的动力，我将会列出所有打赏人员的清单在下方作为凭证，打赏前请留下打赏项目的备注！

![pay.png](https://raw.githubusercontent.com/xuexiangjys/Resource/master/img/pay/pay.png)

## 联系方式

![](https://s1.ax1x.com/2022/04/27/LbGMJH.jpg)
