FROM alpine:3.19

RUN apk add --no-cache openjdk17 maven

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests && \
    cp target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
