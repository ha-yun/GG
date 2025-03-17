# 스테이지 1: 빌드용 Gradle 컨테이너
FROM gradle:jdk17 AS builder

# 워킹 디렉토리(리눅스 기반 설정) 지정
WORKDIR /app

# 호스트 OS에서 백엔드 원소스 전체 카피
# COPY . .
COPY gradlew ./
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./
COPY src/ src/

# gradlew 실행 권한 부여 ✅
RUN chmod +x ./gradlew
RUN ./gradlew clean build --no-daemon -x test
# 최종산출물 : /app/build/libs/*SNAPSHOT.jar


# 스테이지 2: 실행 환경 (JDK 17)
FROM openjdk:17-jdk-slim

# 워킹 디렉토리(리눅스 기반 설정) 지정
WORKDIR /app

# 스테이지1으로부터 특정 위치에 존재하는 jar 현재 컨테이너의 작업디렉토리 루트에 복사
# /app/app.jar
COPY --from=builder /app/build/libs/*SNAPSHOT.jar ./app.jar

# 환경 변수 설정 (Eureka 서버 주소)
ENV EUREKA_SERVER_URL="http://eureka-server:8761/eureka"

ENTRYPOINT ["java", "-jar", "app.jar"]