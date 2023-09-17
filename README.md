# Запуск автотестов

## Последовательность действий для тестирования приложения: 
1. Склонировать репозиторий по [ссылке](https://github.com/Bob-Jacka/Diplom).
2. Открыть проект в IDE
3. Перейте в папку `artifacts` и выполнить команду для бд (в терминале):
- Для БД MySQL:
`docker-compose up -d mysqldb`
- Для БД PostgreSQL:
`docker-compose up -d postgresqldb`
4. Перейте в папку `artifacts` и запустить SUT (отдельный терминал):
- Для MySQL:
`java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./aqa-shop.jar`
- Для PostgreSQL:
`java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar ./aqa-shop.jar`

  Приложение запустится по [ссылке](http://localhost:8080/)
5. Перейте в папку `artifacts` и запустить симулятор банковских сервисов (отдельный терминал):
- `cd ./gate-simulator; npm start`

6. Запустить тесты (отдельный терминал):
- Для БД MySQL:
`./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`
- Для БД PostgreSQL:
`./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
7. Сформировать отчеты Allure:
`./gradlew allureReport`
8. Открыть отчеты в браузере:
`./gradlew allureServe`
9. Остановить приложение, завершите работу allureServe в терминале:
`Ctrl+C`
10. Остановить контейнеры командой
`docker-compose down`
