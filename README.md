# Использование технологии

* Spring Boot
* Webflux (WebClient)
* MockWebServer (для тестирования)

# Код
Приложение обрабатывает ивенты с VK сообщества (сообщение)

Что делает: 
1. Получает ивент с VK сообщества. 
2. Обрабатывает полученный ивент
3. Отправляет сообщение дублирующие его сообщение.

Архитектура:
* [Controller](src/main/java/org/example/botvk/controller/BotVkController.java) - принимает запросы (ивенты)
* [Обработчик ивента](src/main/java/org/example/botvk/event/Vk) (паттерн "Observer"). Настроены для асинхронного выполения 
* [Service](src/main/java/org/example/botvk/service/impl/BotVkServiceImpl.java) - отправляет сообщение пользователю
* [ExceptionHandler](src/main/java/org/example/botvk/exception) - умеет обрабатывать исключения
* [Конфигурация](src/main/java/org/example/botvk/config) - необходимые конфигурации с работой WebClient, многопоточностью
* Использовал jsonSchema2Pojo которая преобразует файл [JSON](src/main/resources/json-schema/VkRequest.json) в классы Java.
* Spring vault - для безопасного использования токена

Тесты:
- MockWebServer
- AssertJ
- JUnit5
- MockMvc
# Как запустить проект

* Клонировать репозиторий
* Установить ngrok (https://dashboard.ngrok.com).
* Создать сообщество в VK и настроить "Работа с API" (https://dev.vk.com/ru/api/bots/getting-started).
* В [application.yaml](src/main/resources/application.yaml) указать необходимые данные: apiVersion, code.
* Настроить токен (установить hashicorp, в командной строке прописать vault kv put secret/bot token="токен")
* Токен нужно скопировать в сообществе ВК

P.S. Если не получается настроить токен, его можно передать в [классе](src/main/java/org/example/botvk/config/WebClientConfig.java) напрямую (Но это не безопасно)