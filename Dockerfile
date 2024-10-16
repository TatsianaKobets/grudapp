# Используем OpenJDK 17 на Alpine
FROM openjdk:17-alpine AS app

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR файл приложения
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/myapp.jar

# Устанавливаем Liquibase и необходимые утилиты
RUN apk add --no-cache unzip curl && \
    curl -L -o liquibase.zip https://github.com/liquibase/liquibase/releases/download/v4.21/liquibase-4.21.zip && \
    unzip liquibase.zip -d /liquibase && \
    rm liquibase.zip

# Копируем файлы с миграциями
COPY liquibase /liquibase

# Выполняем миграции
CMD ["/liquibase/liquibase", "update", "--changeLogFile=/liquibase/changelog.xml", "--url=jdbc:postgresql://postgres:5432/mydb", "--username=user", "--password=password"]

# Открываем порт приложения
EXPOSE 8080

# Запускаем приложение (это будет сделано после миграций)
ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]