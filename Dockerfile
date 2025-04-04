# Fase de construcción
FROM openjdk:17-jdk-slim AS builder

# 1. Creamos el usuario no root
RUN useradd -ms /bin/bash appuser

# 2. Creamos el directorio de trabajo con root
RUN mkdir -p /home/appuser/app

# 3. Copiamos el proyecto como root
COPY . /home/appuser/app

# 4. Asignamos permisos de lectura/escritura a 'appuser'
RUN chown -R appuser:appuser /home/appuser/app

# 5. Cambiamos al usuario 'appuser'
USER appuser

# 6. Ingresamos al directorio del proyecto
WORKDIR /home/appuser/app

# 7. Aseguramos permisos de ejecución para gradlew
RUN chmod +x gradlew

# 8. Compilamos (sin tests) con Gradle Wrapper
RUN ./gradlew clean build -x test --no-daemon

# Fase final (runtime)
FROM openjdk:17-jdk-slim

# Copiamos el JAR desde la fase anterior
COPY --from=builder /home/appuser/app/build/libs/*.jar /app/app.jar

WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
