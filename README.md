# Social Network

### Running with Docker

The easiest way to start the application is using Docker Compose:

```bash
docker-compose up
```

The application will be available at http://localhost:8080. API is available by [openapi.json](src/main/resources/openapi.json).

```shell
apt update && apt install openjdk-21-jdk
jdeps --multi-release 21 --ignore-missing-deps --print-module-deps ./social-network-1.0.0.jar > /tmp/modules.txt && \
    jlink --compress=2 --strip-debug --no-header-files --no-man-pages \
          --add-modules $(cat /tmp/modules.txt) \
          --output jlink-runtime
mvn liquibase:update
./jlink-runtime/bin/java -jar social-network-1.0.0.jar
```
