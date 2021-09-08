#FROM openjdk:12-jdk-alpine
FROM maven:3.8.2-jdk-11

EXPOSE 8080

#COPY data/target/data-0.0.1-SNAPSHOT.jar data.jar
#COPY web/target/web-0.0.1-SNAPSHOT.war web.war
COPY ./. /

#ENTRYPOINT ["java", "-jar", "/web.war"]
CMD ["mvn", "test"]