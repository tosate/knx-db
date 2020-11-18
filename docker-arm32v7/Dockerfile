FROM arm32v7/adoptopenjdk:11-jdk-hotspot
COPY qemu-arm-static /usr/bin/qemu-arm-static
MAINTAINER knx-db@devtom.de
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
RUN mkdir /data
ENTRYPOINT ["java","-jar","/app.jar"]
