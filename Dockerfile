FROM openjdk
MAINTAINER knx-db@devtom.de
EXPOSE 8080
ARG JAR_FILE
COPY ${JAR_FILE} /home/knx-db-service.jar
RUN mkdir /data
CMD ["java","-jar","/home/knx-db-service.jar"]
