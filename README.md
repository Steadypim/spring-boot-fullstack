# Управление клиентами

## Backend
1. Java
2. Spring Boot
3. JUnit
4. Mockito
5. PostgreSQL
6. Flyway
7. Spring Security JWT Auth

## Frontend
1. JS
2. React
3. Formik
4. Chackra UI
5. Vite

## DevOps:
1. Docker
2. Jib
3. GitHub Actions

## Описание проекта

Проект "Управление клиентами" представляет собой веб-приложение, разработанное с использованием Java Spring Boot для бэкенда и JavaScript с React для фронтенда. Основной целью данного проекта является предоставление пользователю возможности эффективно управлять своими клиентами и контактной информацией.

Основные функциональные возможности проекта включают:

1.  Авторизация и аутентификация через JWT: Пользователи могут регистрироваться в системе, а затем аутентифицироваться с помощью JSON Web Token (JWT), обеспечивая безопасность и конфиденциальность данных.
    
2.  Управление клиентами: Зарегистрированные пользователи могут добавлять, просматривать, редактировать и удалять клиентов в системе. Каждый клиент представлен набором полей, таких как имя, фамилия, контактная информация и т.д.

Кроме основных функциональных возможностей, проект "Управление клиентами" также реализует непрерывную интеграцию (CI) с использованием GitHub Actions. CI обеспечивает автоматическую сборку, тестирование приложения при каждом изменении кода, что помогает гарантировать его стабильность и надежность.

Вот основные этапы CI-пайплайна на GitHub Actions для проекта "Управление клиентами":

1.  Сборка (Build): При каждом push или pull request в ветку master GitHub Actions запускает сборку проекта с использованием Maven для бэкенда и npm для фронтенда. Сборка проверяет наличие синтаксических ошибок и компилирует код, готовя его к следующим этапам.
    
2.  Тестирование (Testing): После успешной сборки, проект проходит через автоматический набор тестов, которые включают юнит-тесты и интеграционные тесты. Тесты проверяют корректность работы приложения и обнаруживают возможные ошибки или проблемы.
