k8s部署步骤：

1. 使用maven package将各微服务项目打jar包
2. IDEA配置docker，Dockerfile将微服务打成镜像
3. docker tag对微服务镜像重命名，并push到私有仓库
4. 编写k8s编排文件sboot.yml
5. k8s中使用`kubectl apply -f sboot.yml`

