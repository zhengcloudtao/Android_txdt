# Android_txdt
腾讯地图POI搜索
Android_000_腾讯地图POI搜索_000_分析-2020-8-3
@[TOC](目录)
# 一、效果
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200813162655786.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTA5NjU2OQ==,size_16,color_FFFFFF,t_70#pic_center)

#  二、项目结构
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200813162735603.JPG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTA5NjU2OQ==,size_16,color_FFFFFF,t_70#pic_center)
# 三、需要的包

```java
    implementation fileTree(dir: "libs", include: ["*.jar"])
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation files('libs\\TencentMapSDK_Android_3D_v4.3.3.jar')
    implementation 'com.squareup.okhttp3:okhttp:3.12.0'
    implementation files('src\\main\\jniLibs\\TencentLocationSdk_v7.2.6_rdbae62b0_20200322_210334.jar')
    implementation 'com.tencent.map:tencent-map-vector-sdk:latest.release'
    implementation "androidx.recyclerview:recyclerview:1.1.0"
    implementation "androidx.recyclerview:recyclerview-selection:1.1.0-rc01"
```

# 四、跳转
[Android_000_腾讯地图POI搜索_001_实体类](https://duolc.blog.csdn.net/article/details/107983102)
[Android_000_腾讯地图POI搜索_002_主页面和主活动](https://duolc.blog.csdn.net/article/details/107983244)
[Android_000_腾讯地图POI搜索_003_主页面的适配器和页面](https://duolc.blog.csdn.net/article/details/107983586)
[Android_000_腾讯地图POI搜索_004_搜索页面的适配器和页面](https://duolc.blog.csdn.net/article/details/107983755)
