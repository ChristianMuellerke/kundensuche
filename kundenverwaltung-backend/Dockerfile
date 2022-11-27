FROM registry.access.redhat.com/ubi8/openjdk-17-runtime

USER root

EXPOSE 8080

COPY ./target/demo-service-0.0.1-SNAPSHOT.jar /home/jboss/app.jar

USER 185

RUN java --version

ENTRYPOINT ["java","-jar", "/home/jboss/app.jar"]
