# ![android-sex-seek](art/logo.png)

# android-sex-seek [![Build Status](https://travis-ci.org/dtboy1995/android-sex-seek.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-seek)
:rocket: 最简单的滑块器

# 安装
```gradle
implementation 'org.ithot.android.view:seek:0.1.0'
```

# 屏幕截图

![android-sex-seek](art/snapshot.gif)

# 用法
- XML

```xml
<org.ithot.android.view.SeekView
      xmlns:app="http://schemas.android.com/apk/res-auto"
      android:id="@+id/seek_view"
      android:layout_width="300dp"
      android:layout_height="50dp"
      app:indicatorColor="#ffffff"
      app:indicatorRadius="10dp"
      app:indicatorShadowColor="#cccccc"
      app:indicatorShadowEnable="true"
      app:indicatorShadowRadius="3dp"
      app:onStep="onStep"
      app:seekBackgroundColor="#8e8e93"
      app:seekForegroundColor="#8bc34a"
      app:seekHeight="10dp"
      app:seekShadowColor="#ff0000"
      app:seekShadowEnable="true"
      app:seekShadowRadius="3dp" />
```
- 代码

```java
SeekView sv = (SeekView)findViewById(R.id.seek_view);
// 设置进度值
sv.init(20);
// 使用区间范围设置进度值
sv.init(20, -20, 30);
// 普通进度回调
sv.setSVCallback(new SVCallback() {
    @Override
    // 范围 0 ~ 100
    public void step(int progress) {

    }
    // start() 开始触摸回调
    // end() 停止触摸回调
});
// 区间范围进度回调
sv.setSVCallback(new SVRangeMapCallback(-20, 30) {
    @Override
    // 范围 -20 ~ 30
    public void step(int progress) {

    }
    // start() 开始触摸回调
    // end() 停止触摸回调
});
```

# 属性

名称 | 类型 | 例子 | 描述
:- | :-: | :-: | :-
indicatorRadius | `dimension`&nbsp;`reference` | 10dp&nbsp;@dimen/ | 圆圈半径
indicatorColor | `color`&nbsp;`reference` | #FFFFFF&nbsp;@color/ | 圆圈颜色
indicatorShadowEnable | `boolean` | true&nbsp;false | 开启圆圈的阴影
indicatorShadowRadius | `dimension`&nbsp;`reference` | 2dp&nbsp;@dimen/ | 圆圈阴影的半径
indicatorShadowColor | `color`&nbsp;`reference` | #000000&nbsp;@color/ | 圆圈阴影的颜色
seekHeight | `dimension`&nbsp;`reference` | 7dp&nbsp;@dimen/ | 滑块的高度
seekBackgroundColor | `color`&nbsp;`reference` | #CCCCCC&nbsp;@color/ | 滑块背景色
seekForegroundColor | `color`&nbsp;`reference` | #00FFFF&nbsp;@color/ | 滑块前景色
seekShadowEnable | `boolean` | true&nbsp;false | 开启滑块的阴影
seekShadowColor | `color`&nbsp;`reference` | #000000&nbsp;@color/ | 滑块的阴影颜色
seekShadowRadius | `dimension`&nbsp;`reference` | 2dp&nbsp;@dimen/ | 滑块的阴影半径
touchEnable | `boolean` | true&nbsp;false | 是否可以触摸
onStep | `string` | onStep | 鸡肋功能
