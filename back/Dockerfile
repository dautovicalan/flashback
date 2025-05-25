FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y findutils
RUN ./gradlew clean build -x test
EXPOSE 8080
CMD ["java", "-jar", "./build/libs/flashback-0.0.1-SNAPSHOT.jar"]