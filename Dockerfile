# 1단계: Maven + JDK로 빌드
FROM maven:3.8.1-openjdk-8 AS builder
WORKDIR /app
COPY . .

RUN mvn clean install -P MySQL -DskipTests

# 2단계: Tomcat에 war 복사해서 실행
FROM tomcat:9.0-jdk8
# 기본 webapps 제거 (필요하면)
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/petclinic.war /usr/local/tomcat/webapps/ROOT.war