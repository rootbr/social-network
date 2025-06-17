FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src src

RUN mvn package -DskipTests

RUN jdeps --multi-release 21 --ignore-missing-deps --print-module-deps ./target/social-network-1.0.0.jar > /tmp/modules.txt && \
    jlink --compress=2 --strip-debug --no-header-files --no-man-pages \
          --add-modules $(cat /tmp/modules.txt) \
          --output /app/jlink-runtime

FROM debian:12-slim
WORKDIR /app

COPY --from=build /app/jlink-runtime ./jlink-runtime
COPY --from=build /app/target/social-network-1.0.0.jar app.jar

RUN mkdir -p logs

EXPOSE 8080
ENTRYPOINT ["./jlink-runtime/bin/java", "-jar", "app.jar"]