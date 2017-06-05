# 简影讯
> **这是一枚展示所在城市热映电影资讯和票房的小小应用。简洁的界面高颜值的设计，多色主题自由搭配更有体贴的夜间模式。简约，优雅，精彩，即看即走，全都来自处女座作者的“斤斤计较”。希望你喜欢。o(*￣︶￣*)o**

![poster1](https://github.com/woxingxiao/GracefulMovies/blob/master/images/poster1.jpg)
![poster2](https://github.com/woxingxiao/GracefulMovies/blob/master/images/poster2.jpg)  

这是一个完全`Material Design`风格的Android应用。项目中使用到了`Retrofit`、`RxJava`、`MVP`、`Glide`等方案，换肤框架基于[`Colorful`](https://github.com/garretyoder/Colorful)。图标使用`svg`矢量图，图片使用`webp`格式，最大限度压缩apk尺寸。  

## APK下载
1. bugly（微信、QQ直接安装 ）[beta.bugly.qq.com/jianyingxun](https://beta.bugly.qq.com/jianyingxun)
2. fir.im [https://fir.im/jyx](https://fir.im/jyx)
3. 下列应用商店搜“简影讯”：
   - [x] 应用宝
   - [x] 360手机助手
   - [x] 百度手机助手

## 升级日志  
### 最新：
### 1.3.2 (Build 20)
1. 默认夏日清爽配色主题；
2. 安装包大小优化；
3. 网络日志打印神器：[LoggingInterceptor](https://github.com/ihsanbal/LoggingInterceptor)，与Retrofit一起服用简直不要太爽。

### 历史：
[**UpdateLog**](https://github.com/woxingxiao/GracefulMovies/blob/master/UpdateLog.md)  

## 关于测试
如果你想运行项目，先clone到本地，修改包名，再到聚合数据和[易源数据](https://www.showapi.com/)申请相应的 _AppKey_（需要实名认证，所以私有key不便公开，望见谅）。若嫌麻烦可下载上面链接的apk成品，再参考代码。  
# 致谢
感谢开源的力量！
开源库：  
1. [Colorful](https://github.com/garretyoder/Colorful)  
1. [Retrofit](https://github.com/square/retrofit)  
2. [RxJava](https://github.com/ReactiveX/RxJava)  
3. [RxAndroid](https://github.com/ReactiveX/RxAndroid)  
4. [Glide](https://github.com/bumptech/glide)  
5. [Butter Knife](https://github.com/JakeWharton/butterknife)  
6. [SimpleRatingBar](https://github.com/FlyingPumba/SimpleRatingBar)  
7. [KenBurnsView](https://github.com/flavioarfaria/KenBurnsView)  
8. [BubbleSeekBar](https://github.com/woxingxiao/BubbleSeekBar)  
9. [VectorCompatTextView](https://github.com/woxingxiao/VectorCompatTextView)
10. [SlideBack](https://github.com/oubowu/SlideBack)
11. [LoggingInterceptor](https://github.com/ihsanbal/LoggingInterceptor)

感谢以下的开源项目为本项目提供了诸多参考：
- [StarWars.Android](https://github.com/Yalantis/StarWars.Android)
- [SeeWeather](https://github.com/xcc3641/SeeWeather)

聚合数据提供影讯数据支持。
易源数据提供票房数据支持。
感谢下列酷站和工具为本项目提供帮助：
- [iconfont](http://iconfont.cn) 提供海量矢量图标资源
- [dribbble](https://dribbble.com) 提供海量的图片资源。项目中有两张图片来自[JustinMezzell](https://dribbble.com/JustinMezzell)
- [tinypng](https://tinypng.com) 提供免费并高质量的图片压缩服务
- [智图](http://zhitu.isux.us) 提供图片转`webp`格式服务
- [AndroidAssetStudio](https://romannurik.github.io/AndroidAssetStudio) 提供在线图标制作
- [AndResGuard](https://github.com/shwenzhang/AndResGuard) 资源混淆减小Apk尺寸
- 手机应用软件 _Screener_ 生成漂亮的手机外观截屏图片

**本项目中影讯数据来自聚合数据，版权归聚合数据；票房数据来自易源数据，版权归易源数据。图片资源均收集自网络，版权属于原作者。如有侵权，告知后会立即删除。**
# 彩蛋
本项目在主题界面埋了一颗彩蛋，进入界面会随机加载一段经典电影台词。

![screenshot1](https://github.com/woxingxiao/GracefulMovies/blob/master/images/screenshot1.jpg)

如果你有喜欢的电影台词，不论中外新旧，欢迎添加并`pull request`，有可能会被收录到下个版本里。入口在此：[arrays_quotes_of_movies.xml](https://github.com/woxingxiao/GracefulMovies/blob/master/app/src/main/res/values/arrays_quotes_of_movies.xml)，文件里有详细的格式说明，have fun.
# 结语
本项目出自完美主义+强迫症的处女座作者之手，从界面交互设计，到图片大小优化，到图标的选取，每一处细节都讲究，计较，实为诚（过）意（瘾）之作。如果你喜欢本项目，请右上角**star**鼓励作者，或者推荐给周围的人。

老板们请走VIP打赏通道，金额随意：

![alipay](https://github.com/woxingxiao/GracefulMovies/blob/master/images/alipay.jpg) ![wxpay](https://github.com/woxingxiao/GracefulMovies/blob/master/images/wxpay.jpg)

本人才疏学浅，不足之处欢迎指正。技术交流或合作可邮件：Chauvet910@gmail.com

**作者保留权利**

--------
> **人生苦短，请选择科学上网。推荐一下本人正在使用的，稳定高速，便宜好用。[推介链接](https://portal.shadowsocks.com.hk/aff.php?aff=8881)**

# License
```
   Copyright 2017 woxingxiao

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

