# 酷炫的Tablayout(类似于新浪微博导航栏)
![效果图](https://github.com/ght199266/TabKuLayout/blob/master/app/src/main/image/tt.gif)

### 使用
```java
   //和TabLayout绑定方式一样
   vp_viewpager.setAdapter(new MyPagerAdapter());
   tabKu_layout.setupWithViewPager(vp_viewpager);
```

### Xml
```xml
   <com.lly.mylibrary.TabKuLayout
        android:id="@+id/tk_layout"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        app:tabIndicatorHeight="4dp"
        app:tabIndicator_end_color="#CD3333"
        app:tabIndicator_start_color="#EEC900"
        app:tabSelectedTextColor="#EE0000"
        app:tab_Mode="scrollable"
        app:tab_font_size="14sp" />
```

#### Tips
如果 tabIndicator_end_color 为空的话 则默认显示 tabIndicator_start_color颜色(也就是纯色不渐变)

### 自定义属性
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="TabKuLayout_styleable">
        <!-- 字体大小 -->
        <attr name="tab_font_size" format="dimension" />
        <!-- 字体颜色 -->
        <attr name="tabSelectedTextColor" format="color" />
        <!-- 指示器高度 -->
        <attr name="tabIndicatorHeight" format="dimension" />
        <!--指示器起始颜色 -->
        <attr name="tabIndicator_start_color" format="color" />
        <!--指示器结束颜色 -->
        <attr name="tabIndicator_end_color" format="color" />
        <!-- 显示模式 -->
        <attr name="tab_Mode" format="integer">
            <flag name="scrollable" value="0" />
            <flag name="fixed" value="1" />
        </attr>
    </declare-styleable>
</resources>
```

#### 意见反馈
email: 849851251@qq.com

### Thanks
android.support.design.widget.TabLayout