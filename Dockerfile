FROM openjdk:8
COPY ./build/libs/DBot.jar /tmp/DBot.jar
WORKDIR /tmp
ENTRYPOINT ["java", "-jar", "/tmp/DBot.jar"]
