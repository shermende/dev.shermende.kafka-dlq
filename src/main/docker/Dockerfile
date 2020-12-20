FROM openjdk:11

COPY target/*-runner.jar /application/application.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/application/application.jar"]