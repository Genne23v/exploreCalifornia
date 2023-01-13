FROM openjdk:18.0.2.1-jdk-oraclelinux7
WORKDIR /
ADD target/explorecali-0.0.1-SNAPSHOT.jar //
EXPOSE 8088
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=docker", "/explorecali-0.0.1-SNAPSHOT.jar"]