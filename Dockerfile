# https://stackoverflow.com/questions/46057625/externalising-spring-boot-properties-when-deploying-to-docker
FROM openjdk:11.0.9.1-jdk
MAINTAINER knx-db@devtom.de
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
RUN mkdir /data
ENTRYPOINT ["java","-jar","/app.jar"]
