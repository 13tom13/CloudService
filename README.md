# Cloud service backend
Сервис предоставляет REST-интерфейс для загрузки файлов и вывода списка уже загруженных файлов пользователя.

# Используемые инструменты:
- Spring Boot
- Spring Data JPA
- Liquibase
- Maven
- MySQL
- JUnit 
- Mockito
- MockMvc
- Docker

## Запуск приложения
1. Выполнить команду `mvn clean install`
2. Выполнить команду `docker build -t netology-diplom-backend .`
3. Скачайте [FRONT](https://github.com/netology-code/jd-homeworks/tree/master/diploma/netology-diplom-frontend) (JavaScript)
4. Следуя описанию README.md FRONT проекта, запустите nodejs-приложение (`npm install`, `npm run serve`).
5. Выполнить `docker-compose up`