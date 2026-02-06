FROM maven:3.9.11-eclipse-temurin-25-noble AS build
WORKDIR /app
COPY backend/pom.xml ./backend/pom.xml
COPY backend/src ./backend/src
COPY frontend ./frontend
RUN mvn clean package -DskipTests


FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "app.jar"]
