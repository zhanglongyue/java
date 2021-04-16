FROM java:8

# 作者
MAINTAINER zhanglongyue

# 声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
# VOLUME /tmp

# 复制jar到容器里
ADD target/sboot-auth-server-1.0-SNAPSHOT.jar sboot-auth-server-1.0-SNAPSHOT.jar

# 并不会自动在宿主进行端口映射,只是声明容器打算使用的端口
EXPOSE 9891

# 指定容器启动程序及参数   <ENTRYPOINT> "<CMD>"
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=k8s","sboot-auth-server-1.0-SNAPSHOT.jar"]
