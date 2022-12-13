FROM openjdk:8u141-jre
VOLUME /tmp
ADD target/vrp-preprocess-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT exec java -jar app.jar