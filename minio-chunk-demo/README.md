1.为了简单操作，这里只设计了一张表，即存放文件的表，放在了doc文件夹下，自行参考

2.代码的具体实现思路逻辑参考我的文章：[minio&前后端分离上传视频/上传大文件——前后端分离断点续传&minio分片上传实现](https://blog.csdn.net/qq_62982856/article/details/129002288)

3.MediaFileServiceImpl.java 中的uploadMergeChunks方法，里面有向数据库增加文件记录的操作，设置上传者id时我设置为了定值，实际开发中应当根据token拿到当前用户的Id。