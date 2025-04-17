# Zoo Management System

## Реализованный функционал

| Функционал                     | Реализация                                                                 |
|--------------------------------|----------------------------------------------------------------------------|
| Добавление/удаление животного  | `AnimalService`, `AnimalController`                                        |
| Добавление/удаление вольера    | `EnclosureService`, `EnclosureController`                                  |
| Перемещение животных           | `AnimalTransferService`, `AnimalTransferController`                        |
| Расписание кормлений           | `FeedingScheduleService`, `FeedingOrganizationService`, `FeedingScheduleController` |
| Просмотр статистики            | `ZooStatisticsService`, `ZooStatisticsController`                         |
| Доменные события               | `AnimalMovedEvent`, `FeedingTimeEvent`                                     |

## Примененные архитектурные принципы

### Domain-Driven Design (DDD)

| Концепция                     | Примеры реализации                                                                 |
|-------------------------------|-----------------------------------------------------------------------------------|
| Богатые доменные модели       | `Animal`, `Enclosure`, `FeedingSchedule` с бизнес-логикой                        |
| Value Objects                 | `AnimalName`, `AnimalBirthday`, `FeedingTime`, `EnclosureMaxCapacity`            |
| Доменные события              | `AnimalMovedEvent`, `FeedingTimeEvent`                                           |
| Ограниченные контексты        | Четкое разделение на животных, вольеры и расписания                              |

### Clean Architecture

| Принцип                      | Реализация                                                                |
|------------------------------|---------------------------------------------------------------------------|
| Слоистая архитектура         | `domain`, `application`, `infrastructure`, `presentation`                 |
| Зависимости внутрь           | Domain не зависит от других слоев                                         |
| Инверсия зависимостей        | Прослойки между контроллерами, сервисами и репозиториями                  |
| Изоляция бизнес-логики       | Вся логика в Domain и Application слоях                                   |
| Адаптеры для внешних систем  | Репозитории как адаптеры к БД (`AnimalRepository`, `EnclosureRepository`) |

## Структура проекта

```
src/
├── main/
│   ├── java/
│   │   ├── domain/                # Ядро системы
│   │   │   ├── model/             # Сущности и VO
│   │   │   └── events/            # Доменные события
│   │   │
│   │   ├── application/           # Бизнес-логика
│   │   │   ├── services/          # Сервисы
│   │   │   ├── config/            # Конфигурация приложения
│   │   │   ├── ports/             # Интерфейсы
│   │   │   └── exceptions/        # Исключения и их обработка
│   │   │
│   │   ├── infrastructure/        # Внешние системы
│   │   │   ├── repositories/      # Репозитории
│   │   │   ├── converters/        # Конвертеры
│   │   │   └── listeners/         # Слушатели событий
│   │   │
│   │   └── presentation/          # API и Web
│   │       ├── controllers/       # REST контроллеры
│   │       └── dto/               # DTO для API
│   │
│   └── resources/                 # Конфигурации и миграции
└── test/                          # Полноценные тесты сервисного слоя
```

## Технологический стек

- **Язык**: Java 17
- **Фреймворк**: Spring Boot 3
- **База данных**: H2 (in-memory)
- **Тестирование**: JUnit 5, Mockito

## Как запустить проект

1. Клонировать репозиторий
2. Собрать проект: `mvn clean install`
3. Запустить: `mvn spring-boot:run`
4. Протестировать через Postman (Импортировать ZooHM2HSE.postman_collection.json из корня проекта в Postman)
5. `ОПЦИОНАЛЬНО` Запуск тестов с помощью `mvn clean test`

## Что сделано

- Полная реализация всех требований и бизнес-логики
- Соответствие DDD и Clean Architecture
- 65%+ покрытие кода тестами
- Создана Postman коллекция для тестирования эндпоинтов
- Гибкая и масштабируемая система

