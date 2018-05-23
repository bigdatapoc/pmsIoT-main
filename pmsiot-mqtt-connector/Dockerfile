FROM java:8
VOLUME /tmp
ADD target/pmsiot-mqtt-connector-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8095 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]