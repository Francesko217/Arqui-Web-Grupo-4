FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/TuTrade-0.0.1-SNAPSHOT.jar app.jar
COPY start.sh start.sh
RUN chmod +x start.sh
EXPOSE 8080
ENTRYPOINT ["bash", "start.sh"]
