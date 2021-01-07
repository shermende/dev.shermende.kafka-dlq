FROM openjdk:11

COPY . /application

WORKDIR /application

RUN echo "wait several minutes while building" && ./mvnw clean package

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","target/application.jar"]