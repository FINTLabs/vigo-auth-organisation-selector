FROM ghcr.io/fintlabs/vigo-auth-organisation-selector-frontend:sha-98d01e9 as frontend

FROM gradle:7.1.1-jdk11 as builder
USER root
COPY . .
COPY --from=frontend /html/ src/main/resources/public/
RUN gradle --no-daemon clean build

# FROM gcr.io/distroless/java:11
FROM openjdk:11.0-jre
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/vigo-auth-organisation-selector-*.jar /data/vigo-auth-organisation-selector.jar
CMD ["java","-jar","/data/vigo-auth-organisation-selector.jar"]
