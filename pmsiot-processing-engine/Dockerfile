FROM williamyeh/scala
RUN \
  apt-get update && \
  apt-get install -y openjdk-8-jdk && \
  java -version
VOLUME /tmp
ADD target/pmsiot-processing-engine-0.0.1-SNAPSHOT-jar-with-dependencies.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8072
ENTRYPOINT ["java", "-jar", "/app.jar"]