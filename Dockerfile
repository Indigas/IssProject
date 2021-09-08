FROM openjdk:12-jdk-alpine

EXPOSE 8080

COPY data/target/data-0.0.1-SNAPSHOT.jar data.jar
COPY web/target/web-0.0.1-SNAPSHOT.war web.war

ENTRYPOINT ["java", "-jar", "/web.war"]
