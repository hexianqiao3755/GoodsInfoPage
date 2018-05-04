# GoodsInfoPage
##### 仿京东、天猫app的商品详情页的布局架构, 以及功能实现
##### 类似的架构可以自行修改, 代码中有注释
___
![Travis CI](https://travis-ci.org/DreaminginCodeZH/Douya.svg)
##### 有需要做电商类app的可以看看, 首先先看看效果实现

![效果实现](https://github.com/hexianqiao3755/GoodsInfoPage/tree/master/data/demo.gif)

也可以[点击这里下载](https://github.com/hexianqiao3755/GoodsInfoPage/tree/master/data/app-debug.apk)

## 配置
在项目**build.gradle**中添加依赖：
```
allprojects {
    repositories {
        jcenter()
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.facebook.fresco:fresco:0.9.0'
    compile 'com.gxz.pagerslidingtabstrip:library:1.3'
    compile 'com.bigkoo:convenientbanner:2.0.5'
}
```

## 反馈
欢迎各位提issues和PRs

## 联系我
_hexianqiao3755@gmail.com_

## 第三方库
- [Fresco](https://github.com/facebook/fresco)
- [ConvenientBanner](https://github.com/saiwu-bigkoo/Android-ConvenientBanner)
- [PagerSlidingTabStrip](https://github.com/astuetz/PagerSlidingTabStrip)

## 许可证

    Copyright 2017 He Qiao

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.