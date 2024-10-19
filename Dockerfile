# Используем базовый образ с JDK
#FROM openjdk:17-jdk-slim

# Установка Maven
#RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
# Устанавливаем рабочую директорию
#WORKDIR /app

# Копируем файл pom.xml и загружаем зависимости
#COPY pom.xml .
#RUN mvn dependency:go-offline

# Копируем исходный код проекта
#COPY src ./src

# Строим проект
#RUN mvn package

# Указываем команду для запуска приложения
#ENTRYPOINT ["java", "-jar", "target/grudapp-1.0-SNAPSHOT.jar"]