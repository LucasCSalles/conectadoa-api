FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /opt/app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app
COPY --from=build /opt/app/target/app.jar /opt/app/app.jar
ENV SPRING_PROFILES_ACTIVE=prd
EXPOSE 8080
CMD ["/bin/sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"]
