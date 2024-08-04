# Solva Test Service

## Описание
Этот сервис предоставляет API для управления транзакциями и лимитами расходов.

## Требования
- Java 11 или выше
- Maven 3.6 или выше

## Установка и запуск
1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/your-repository.git
    cd your-repository
    ```

2. Соберите проект с помощью Maven:
    ```bash
    mvn clean install
    ```

3. Запустите приложение:
    ```bash
    mvn spring-boot:run
    ```

## Документация API
После запуска приложения, документация Swagger будет доступна по следующему URL:


http://localhost:8080/swagger-ui.html



### Endpoints
#### Transaction Management
- **POST /api/transactions**
    - Описание: Добавить новую транзакцию
    - Пример запроса:
      ```json
      {
        "amount": 500,
        "currency": "KZT",
        "transactionDate": "2022-01-02",
        "category": "Food"
      }
      ```

- **GET /api/transactions/exceeded**
    - Описание: Получить транзакции, превысившие лимит

#### Expense Limit Management
- **POST /api/limits**
    - Описание: Установить новый лимит расходов
    - Пример запроса:
      ```json
      {
        "category": "Food",
        "amount": 1000,
        "startDate": "2022-01-01"
      }
      ```

- **GET /api/limits**
    - Описание: Получить все лимиты расходов
