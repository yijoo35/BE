# 1단계: 빌드 스테이지 (JDK 21)
FROM gradle:8.6-jdk21 AS builder
WORKDIR /app

# Gradle 캐시 최적화
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle dependencies --no-daemon || true

# 프로젝트 전체 복사
COPY . .

# Spring Boot JAR 빌드 (테스트 제외)
RUN gradle clean build -x test --no-daemon

# 2단계: 실행 스테이지 (JDK 21 런타임)
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/seniorjob-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# JVM 메모리 최적화
ENTRYPOINT ["java", \
    "-Xmx768m", \
    "-Xms256m", \
    "-Dspring.profiles.active=prod", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "/app/app.jar"]