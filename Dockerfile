# https://stackoverflow.com/questions/46057625/externalising-spring-boot-properties-when-deploying-to-docker
FROM adoptopenjdk:11.0.9.1_1-jdk-hotspot-focal
COPY qemu-arm-static /usr/bin/qemu-arm-static
MAINTAINER knx-db@devtom.de
VOLUME /tmp
EXPOSE 8080
COPY ./target/knx-db-service.jar app.jar
RUN mkdir /data
ENTRYPOINT ["java","-jar","/app.jar"]
