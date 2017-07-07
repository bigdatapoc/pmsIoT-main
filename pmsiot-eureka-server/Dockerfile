FROM java:8
VOLUME /tmp
ADD target/pmsiot-eureka-server-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app.jar"]