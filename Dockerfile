#ADD something to execute .sh

FROM maven:3.5-jdk-8-alpine as BUILD
COPY src /usr/src/rhizome-coins/src
COPY pom.xml /usr/src/rhizome-coins
WORKDIR /usr/src/rhizome-coins
RUN mvn dependency:resolve
RUN mvn -f pom.xml clean install

FROM openjdk:8-jre-alpine
RUN apk add --no-cache bash
COPY --from=BUILD /usr/src/rhizome-coins/target/rhizome-coins.jar /usr/src/execute/
COPY config.yml /usr/src/execute/
COPY rhizome-coins-backend.sh /usr/src/execute/
RUN chmod -R 755 /usr/src/execute
EXPOSE 8080
WORKDIR /usr/src/execute
CMD bash rhizome-coins-backend.sh