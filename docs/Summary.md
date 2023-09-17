# Отчёт о проведённой автоматизации сервиса
### Что было запланировано и что реализовано
Было запланировано и реализовано следующее:
- автоматизировано тестирование сервиса по покупке тура с возможностью оплаты 2 способами - дебетовой картой или выдачей кредита по данным банковской карты;
- были прописаны 6 позитивных и 74 негативных сценария;
- реализованы UI-тесты (80) с запросами в базу, проверяющими корректность внесения информации приложением в случаях оплаты картами со статусами APPROVED и DECLINED;
- реализована поддержка двух баз данных - MySQL и PostgreSQL.
### Сработавшие риски
Большая часть сложностей и потраченное время связаны с реализацией следующих работ, вызванных упомянутыми в Плане автоматизации тестирования рисками:
- Отсутствия документации к приложению;
- Настройка одновременной поддержки MySQL/PostgreSQL.
- Подбор селекторов для выборки элементов веб-приложения в условиях отсутствия селекторов data-test-id;
### Общий итог по времени: сколько запланировали и сколько выполнили с обоснованием расхождения
Было запланировано провести работу за 64 часов, а с учетом реализации всех рисков - до 96 часов (+50% времени).
Реально затрачено - 80 часов, при этом на планирование автоматизации тестирования ушло вместо 10 запланированных часов - около 8, а на автоматизацию - вместо 40 часов - около 30.
Заведение багов заняло 4 часов, что на 3 часа меньше, чем было указано в плане, а подготовка отчетных документов - 3 вместо запланированных 7 часов соответственно.