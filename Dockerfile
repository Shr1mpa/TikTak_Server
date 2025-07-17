
FROM gradle:8.4.0-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle installDist


FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/install/TikTak_Server ./TikTak_Server
EXPOSE 8080
CMD ["./TikTak_Server/bin/TikTak_Server"]