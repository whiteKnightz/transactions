#FROM openjdk:8-jdk-alpine
#VOLUME /tmp
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.Clusus.transactions.TransactionsApplication"]

#FROM openjdk:8-jdk-alpine
#VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} transactions-0.1.0.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/transactions-0.1.0.jar"]
FROM openjdk:8
ADD target/transactions.jar transactions.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","transactions.jar"]
