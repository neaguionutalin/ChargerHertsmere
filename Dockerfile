ARG VERSION=21
FROM openjdk:${VERSION}-jdk as BUILD
COPY  . /home/app/
WORKDIR /home/app
RUN chmod 777 -R /home/app/
RUN chmod +x ./gradlew
RUN ls
RUN ./gradlew clean bootJar

FROM openjdk:${VERSION}
COPY --from=build /home/app/build/libs/*.jar /app/service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=override", "/app/service.jar"]