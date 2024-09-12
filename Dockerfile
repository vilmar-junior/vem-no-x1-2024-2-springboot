#
# Build stage
#
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean install

#
# Package stage
#
COPY --from=build /target/vemnox1-0.0.1-SNAPSHOT.jar vemnox1.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","vemnox1.jar"]