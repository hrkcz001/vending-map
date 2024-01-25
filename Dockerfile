FROM gradle:8.5-jdk21-alpine AS buildServerSide

WORKDIR /app
COPY server/build.gradle server/settings.gradle ./
COPY server/src ./src

RUN gradle build --no-daemon

FROM node:14 AS buildFrontend

WORKDIR /app
COPY WebApp .

RUN yarn install
RUN yarn webpack --mode production

FROM openjdk:21-rc-slim
LABEL key="vending-map-server"

WORKDIR /app
COPY static static
COPY --from=buildServerSide /app/build/libs/server.jar server.jar
COPY --from=buildFrontend /app/dist static

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]