FROM openjdk:21-jdk-slim
WORKDIR /app
COPY api/build/libs/*.jar app.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "sock-0.0.1-SNAPSHOT.jar"]