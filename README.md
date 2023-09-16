# Запуск автотестов

## Последовательность действий для тестирования: 
1. Склонировать репозиторий по [ссылке](https://github.com/Bob-Jacka/Diplom).
2. Открыть проект в IDE
3. Перейте в папку `artifacts` (Все дальнейшие действия выполняются в этой папке)
4. Запустить базу данных командой:
`docker-compose up`
5. В зависимости от базы данных, запустить SUT (в терминале):
- Для MySQL:
`java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./aqa-shop.jar`
- Для PostgreSQL:
`java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar ./aqa-shop.jar`

  Приложение запустится по [ссылке](http://localhost:8080/)
6. Запустить симулятор банковских сервисов (отдельный терминал):
- `cd ./gate-simulator; npm start`

7. Запустить тесты (отдельный терминал):
- Для БД MySQL:
`./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`
- Для БД PostgreSQL:
`./gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
8. Сформировать отчеты Allure:
`./gradlew allureReport`
9. Открыть отчеты в браузере:
`./gradlew allureServe`
10. Остановить приложение, завершить работу allureServe в терминале:
`Ctrl+C`
11. Остановить контейнер командой
`docker-compose down`
