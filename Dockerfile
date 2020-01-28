FROM maven:3.5.4-jdk-8-alpine as BUILD
COPY src /usr/src/helloworld/src
COPY pom.xml /usr/src/helloworld
WORKDIR /usr/src/helloworld
RUN mvn clean package

FROM tomcat:9.0
COPY --from=BUILD /usr/src/helloworld/target/hello-world-1.0.war /usr/local/tomcat/webapps/hello-world.war
EXPOSE 8080
