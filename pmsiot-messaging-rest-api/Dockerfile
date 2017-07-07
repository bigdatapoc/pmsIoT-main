FROM java:8
VOLUME /tmp
ADD target/pmsiot-messaging-rest-api-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]