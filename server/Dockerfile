FROM openjdk:11-jdk-slim
LABEL authors="morozan1"

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .

COPY src src

RUN ./gradlew build

EXPOSE 8080

CMD ["java", "-jar", "build/libs/dev-morozan1-server.jar"]