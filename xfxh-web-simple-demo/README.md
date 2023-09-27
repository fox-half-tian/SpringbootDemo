# 讯飞星火大模型后端接口

启动项目后，直接使用 GET 请求访问 http://localhost:8080/test/sendQuestion?question=hello

讯飞官方web文档：https://www.xfyun.cn/doc/spark/Web.html

# 说明
该后端接口的大致实现逻辑：

1. 以 GET 方式访问 SpringBoot 后端接口；
2. 根据你的配置信息生成通用**鉴权URL**，并携带 question 建立 **websocket 连接**；
3. 星火大模型**流式返回**生成的回答；
4. 当大模型返回给后端的响应中出现 **已返回全部回答的标识status** 后，后端关闭 websocket 连接；
5. 后端将生成的完整回答响应给接口调用者。

如果你想了解更详细的与星火大模型之间的参数说明，请参考 [星火认知大模型Web文档](https://www.xfyun.cn/doc/spark/Web.html)

该项目后端接口的实现功能：

- 能回答单个问题，但不支持上下文；
- 对星火大模型限制的 QPS 做了处理；
- 通过配置文件可以规定大模型回复问题的最大响应时长；

如果想要使用支持上下文的接口，只需要找到 **xfxh-web-support-context-demo** 模块，它在 **xfxh-web-simple-demo** 模块基础上实现了基于上下文的回答，该增强模块的后端接口说明：

- 将上下文内容信息保存到了内存中，可以通过配置文件设置保存的上下文内容条数以及用户信息数；
- 一份交互记录指的是两条上下文内容，分别是用户的问题和大模型的回答；
- 支持了唯一标识的用户必须先等他的上一条问题的回答生成才能发送新的问题；
- 由于信息存储在内存中，因此设置了定时任务检查用户是否过期并移除，这个过期时间可以在配置文件中设置。

代码还是易懂的，如果想了解如何实现的，建议先看完有完整注释的 **xfxh-web-simple-demo** 模块，再去看 **xfxh-web-support-context-demo** 模块。**xfxh-web-support-context-demo** 模块只是在 **xfxh-web-simple-demo** 模块进行了补充/增强。 

