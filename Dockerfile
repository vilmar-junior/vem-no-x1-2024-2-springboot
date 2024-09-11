# Stage 1: Build the application with Maven
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml .
COPY src ./src

# Build the application and copy dependencies
RUN mvn clean package -DskipTests && \
    mkdir -p dependency-jars && \
    mvn dependency:copy-dependencies -DoutputDirectory=dependency-jars

# Stage 2: Create the final image with Tomcat
#FROM tomcat:10.1-jdk11-openjdk-slim
FROM tomcat:jdk17


# Remove default Tomcat applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR file from the build stage to Tomcat's webapps directory
COPY --from=build /app/target/vemnox1-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copy the dependencies from the build stage to Tomcat's lib directory
COPY --from=build /app/dependency-jars /usr/local/tomcat/lib/

# Expose the port your app runs on
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]
