FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} course-helper-api.jar
ENTRYPOINT ["java","-jar","/course-helper-api.jar"]