FROM openjdk:12-jdk-alpine

EXPOSE 8080

RUN adduser -D tweetie

RUN mkdir companies && chown tweetie companies

COPY --chown=tweetie data/target/data-0.0.1-SNAPSHOT.jar data.jar
COPY --chown=tweetie web/target/web-0.0.1-SNAPSHOT.war web.war

USER tweetie

ENTRYPOINT ["java", "-jar", "/web.war"]
