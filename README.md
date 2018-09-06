
# Talk——一个说话的软件

演示
=========

功能
=========
   论坛：添加帖子、评论帖子、赞同、反对

   心情记录：本地存储，绘制图表显示

   语言聊天室：语言聊天

   个人中心: 登录、注册、退出登录

实现
=========
1. 网络连接通过okhttp，解析json数据存储在List<Map<String,Object>>列表中。
2. 底部菜单栏使用bottom-navigation-bar
3. RxPermissions是帮助开发者简化requestPermissions()相关的处理。
4. SVProgressHUD弹出加载动画
5. FilterMenu弹出动画菜单
6. 文字路径动画控件TextPathView
7. 图片加载库glide:图片的缓存和加载
8. circlerefreshes项目顶部下拉刷新
9. hellocharts图表绘制
10. voicerecorder语言记录项目

总结
========
上学期第一次做得Android项目，基本上所有控件都是扒别人现成的功能和动画。初次前端和后台交互，解决一些数据类型和存储方式的问题。
