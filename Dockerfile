FROM gradle:8.5-jdk21-alpine AS buildServerSide

WORKDIR /app
COPY server/build.gradle server/settings.gradle ./
COPY server/src ./src

RUN gradle build -x test --no-daemon

FROM openjdk:21-rc-slim
LABEL key="vending-map-server"

WORKDIR /app
COPY --from=buildServerSide /app/build/libs/server.jar server.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]