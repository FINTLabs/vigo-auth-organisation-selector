FROM fintlabsacr.azurecr.io/vigo-auth-organisation-selector-frontend as node

FROM gradle:7.1.1-jdk11 as builder
USER root
COPY . .
COPY --from=node /src/build/ src/main/resources/public/
RUN gradle --no-daemon clean build

FROM gcr.io/distroless/java:11
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/vigo-auth-organisation-selector-*.jar /data/vigo-auth-organisation-selector.jar
CMD ["/data/vigo-auth-organisation-selector.jar"]