# Универсальное CRUD-приложение на Java

![Screenshot](screenshot.png) *(добавьте скриншот интерфейса)*

## 📋 Описание
Приложение для управления сущностями с базовым функционалом:
- **Create** (добавление)
- **Read** (просмотр с пагинацией и поиском)
- **Update** (редактирование)
- **Delete** (удаление)

## 🛠 Технологии
- Java 17+
- JavaFX (для GUI)
- SQLite (база данных)
- Maven (сборка)

## 🚀 Запуск
### Способ 1: Из IDE
1. Убедитесь, что установлена JDK 17+.
2. Добавьте VM Options для JavaFX: --module-path "путь_к_javafx_sdk/lib" --add-modules javafx.controls,javafx.fxml
3. 3. Запустите `Main.java`.

### Способ 2: Из JAR-файла
1. Соберите JAR:
```bash
mvn clean package
2. Запустите: java -jar target/crud-app-jar-with-dependencies.jar

📦 Структура БД
Таблица entities:

Поле	Тип	Описание
id	UUID	Уникальный идентификатор
name	TEXT	Название (3-50 символов)
description	TEXT	Описание (до 255 символов)
createdAt	TIMESTAMP	Дата создания
updatedAt	TIMESTAMP	Дата обновления
