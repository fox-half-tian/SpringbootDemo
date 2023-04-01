## 1.application.yml 文件中的配置需要进行修改。

- redis 地址
- redis 密码
- 短信平台信息

## 2.ubuntu安装docker的步骤

1. 安装需要的包

   ```shell
   sudo apt-get update
   ```

2. 安装依赖包

   ```shell
   sudo apt-get install \
      apt-transport-https \
      ca-certificates \
      curl \
      gnupg-agent \
      software-properties-common
   ```

3. 添加 Docker 的官方 GPG 密钥

   ```shell
   curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
   ```

4. 设置远程仓库

   ```shell
   sudo add-apt-repository \
      "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
     $(lsb_release -cs) \
     stable"
   ```

5. 安装 Docker-CE

   ```shell
   sudo apt-get update
   
   sudo apt-get install docker-ce docker-ce-cli containerd.io
   ```

6. 验证是否成功

   ```shell
   sudo docker run hello-world
   ```

## 3.使用 docker 安装redis并设置密码的步骤

 ```shell
# 拉取redis镜像
docker pull redis

# 启动容器的时候，并为其设置密码
docker run -d --name myredis -p 6379:6379 redis --requirepass "123456"
 ```

## 其它注意事项

注意需要将防火墙6379端口打开。