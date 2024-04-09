FROM gradle:7.3.1-jdk11 AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle shadowJar --no-daemon

FROM openjdk:11-jre-slim

COPY --from=build /home/gradle/src/build/libs/kedubak-1.0-SNAPSHOT.jar /app.jar

EXPOSE 7070

ENTRYPOINT ["java", "-jar", "/app.jar"]