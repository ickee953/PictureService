FROM alpine:3.20.0

RUN apk add openjdk17
COPY target/picture_service-1.0-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]