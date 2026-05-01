# Task Time Tracker API

## Описание

REST-сервис для учёта рабочего времени сотрудников. Позволяет создавать задачи, управлять их статусами и фиксировать временные отрезки, затраченные сотрудниками на выполнение задач.

## Стек технологий

- Java 21
- Spring Boot 3.2.5
- Spring Security + JWT (jjwt 0.12.5)
- MyBatis 3.0.3
- MapStruct 1.5.5
- Liquibase
- PostgreSQL 16
- SpringDoc OpenAPI (Swagger) 2.5.0
- JUnit 5 + Mockito
- TestContainers
- Docker / Docker Compose
- Maven
- 
## Документация

- `DECISIONS.md` — обоснование принятых архитектурных решений
- `AI_INTERACTION.md` — артефакты взаимодействия с ИИ в процессе разработки
- `postman_collection.json` — коллекция запросов для проверки всех эндпоинтов

## Реализованный функционал

**Задачи (Task):**
- Создание задачи
- Получение задачи по ID
- Изменение статуса задачи (NEW → IN_PROGRESS → DONE)

**Записи о времени (TimeRecord):**
- Создание записи о затраченном времени сотрудника на задачу
- Получение отчёта о затраченном времени сотрудника за период с подсчётом суммарных минут (учитываются частично пересекающиеся записи)

**Безопасность:**
- Bearer Authentication (JWT)
- Все эндпоинты кроме `/api/auth/login` требуют авторизации

**Дополнительно:**
- Валидация входных DTO (Bean Validation)
- Обработка исключений через `@RestControllerAdvice`
- Документация API через Swagger UI

## Запуск приложения

### 1. Требования

- Java 21+
- Maven 3.8+
- Docker + Docker Compose

### 2. Поднять базу данных

```bash
docker compose up -d
```

### 3. Собрать проект

```bash
mvn clean install -DskipTests
```

### 4. Запустить приложение

```bash
mvn spring-boot:run
```

Приложение запустится на `http://localhost:8080`

## Проверка работоспособности

### Swagger UI

После запуска откройте в браузере:
```
http://localhost:8080/swagger-ui/index.html
```

Нажмите кнопку **Authorize** и введите Bearer токен для работы с защищёнными эндпоинтами.

### Тестовые запросы

К проекту приложена коллекция Postman — файл `postman_collection.json` в корне проекта.
Импортируйте её в Postman и используйте для проверки всех эндпоинтов.

## Тесты

### Unit-тесты

```bash
mvn test
```

Покрыты сервисы `TaskServiceImpl` и `TimeRecordServiceImpl` — позитивные и негативные сценарии для каждого метода.

### Интеграционные тесты

Написаны для DAO-слоя (`TaskMapper`, `TimeRecordMapper`) с использованием TestContainers. Для запуска требуется совместимая версия Docker окружения.

## Структура проекта

```
src/main/java/com/sdek/tasktimetracker/
├── config/          # SecurityConfig, OpenApiConfig
├── controller/      # TaskController, TimeRecordController, AuthController
├── exception/       # Кастомные исключения, GlobalExceptionHandler
├── mapper/
│   ├── dto/         # MapStruct мапперы (DTO ↔ Entity)
│   └── mybatis/     # MyBatis мапперы (работа с БД)
├── model/
│   ├── dto/
│   │   ├── request/ # DTO входящих запросов
│   │   └── response/# DTO исходящих ответов
│   ├── entity/      # Task, TimeRecord
│   └── enums/       # TaskStatus
├── security/        # JwtService, JwtAuthFilter
└── service/
    ├── impl/        # TaskServiceImpl, TimeRecordServiceImpl
    ├── TaskService
    └── TimeRecordService
```