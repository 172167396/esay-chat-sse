FROM java:8
# 作者
MAINTAINER dailu
ENV MYPATH /home/dailu
WORKDIR $MYPATH
# ADD 命令必须提供两个参数，第一个参数为：宿主机中的目录，相对于Dockerfile文件
# 第二个参数为容器中的目录，相对于WORKDIR
ADD ./target/sse.jar sse-chat.jar
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的$MYPATH
VOLUME $MYPATH

EXPOSE 10088
# 运行jar包
RUN bash -c 'touch ./sse-chat.jar'
ENTRYPOINT ["java","-jar","./sse-chat.jar"]