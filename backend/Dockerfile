FROM maven:3.9.11-eclipse-temurin-25-noble AS build
WORKDIR /app
COPY pom.xml ./pom.xml
COPY src ./src
COPY frontend ./frontend
RUN mvn clean package -DskipTests


FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
