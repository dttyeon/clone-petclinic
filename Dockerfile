# 1단계: Maven + JDK로 빌드
FROM maven:3.8.1-openjdk-8 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install -P MySQL -DskipTests

# 2단계: Tomcat에 war 복사해서 실행
FROM tomcat:9.0-jdk8
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/petclinic.war /usr/local/tomcat/webapps/ROOT.war

RUN mkdir -p /whatap
COPY --from=whatap/kube_mon /data/agent/micro/whatap.agent.kube.jar /whatap/
COPY ./whatap.conf /whatap/

# 2-4. Tomcat JVM 옵션에 -javaagent 추가하기 위한 setenv.sh
RUN mkdir -p /usr/local/tomcat/bin
RUN echo 'CATALINA_OPTS="$CATALINA_OPTS -javaagent:/whatap/whatap.agent.kube.jar -Dwhatap.micro.enabled=true -Dwhatap.conf=/whatap/whatap.conf"' > /usr/local/tomcat/bin/setenv.sh \
 && chmod +x /usr/local/tomcat/bin/setenv.sh

# 2-5. 포트 노출
EXPOSE 8080

# 2-6. 기본 ENTRYPOINT는 유지
CMD ["catalina.sh", "run"]