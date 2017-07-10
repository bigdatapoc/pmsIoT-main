FROM java:8
VOLUME /tmp
ADD target/pmsiot-operations-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app.jar"]