FROM openjdk:8
# 作者
MAINTAINER dailu
ENV MYPATH /home/socket-chat

ARG buildName
ARG dsUrl
ARG dsName
ARG dsPwd
ARG redisHost
ARG redisPwd
ARG minioUrl
ARG minioName
ARG minioPwd

ENV BUILDNAME=$buildName
ENV DSURL=$dsUrl
ENV DSNAME=$dsName
ENV DSPWD=$dsPwd
ENV REDISHOST=$redisHost
ENV REDISPWD=$redisPwd
ENV MINIOURL=$minioUrl
ENV MINIONAME=$minioName
ENV MINIOPWD=$minioPwd


WORKDIR $MYPATH
# ADD 命令必须提供两个参数，第一个参数为：宿主机中的目录，相对于Dockerfile文件
# 第二个参数为容器中的目录，相对于WORKDIR

ADD ./$BUILDNAME.jar sse-chat.jar
# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的$MYPATH
VOLUME $MYPATH
#RUN echo "-Dspring.datasource.url=\"$dsUrl\" -Dspring.datasource.username=$dsName -Dspring.datasource.password=$dsPwd -Dspring.redis.host=$redisHost -Dspring.redis.password=$redisPwd -Dminio.endpoint=\"$minioUrl\" -Dminio.accessKey=$minioName -Dminio.secretKey=$minioPwd"
EXPOSE 10088

# 运行jar包
RUN bash -c 'touch ./sse-chat.jar'

ENTRYPOINT java -Dspring.datasource.url="$DSURL" -Dspring.datasource.username=$DSNAME -Dspring.datasource.password=$DSPWD -Dspring.redis.host=$REDISHOST -Dspring.redis.password=$REDISPWD -Dminio.endpoint="$MINIOURL" -Dminio.accessKey=$MINIONAME -Dminio.secretKey=$MINIOPWD -jar ./sse-chat.jar