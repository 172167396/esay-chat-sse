FROM java:8
# 作者
MAINTAINER dailu
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中并更名为app.jar
ADD ./target/sse.jar /home/dailu/
WORKDIR /hone/dailu/
EXPOSE 10088
# 运行jar包
RUN bash -c 'touch /sse-chat.jar'
ENTRYPOINT ["java","-jar","/sse-chat.jar"]