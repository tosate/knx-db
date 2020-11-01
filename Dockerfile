FROM openjdk
MAINTAINER devtom.de
EXPOSE 8080
ARG JAR_FILE
COPY ${JAR_FILE} /home/knx-db-service.jar
CMD ["java","-jar","/home/knx-db-service.jar"]
