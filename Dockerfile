# Этап сборки
FROM gradle:8.8-jdk17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы сборки Gradle
COPY build.gradle settings.gradle ./

# Загружаем зависимости
RUN gradle build --no-daemon --parallel --stacktrace -x test

# Копируем исходные файлы
COPY src/ src/

# Выполняем сборку
RUN gradle clean build --no-daemon --stacktrace -x test

# Этап выполнения
FROM openjdk:17-jdk-slim

# Копируем скомпилированный jar из предыдущего этапа
COPY --from=build /app/build/libs/*.jar application.jar

# Открываем порт
EXPOSE 8082

# Указываем команду запуска приложения
ENTRYPOINT ["java", "-jar", "application.jar"]
