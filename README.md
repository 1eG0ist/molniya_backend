Как использовать

# Запуск (docker-compose, dev, prod)

Запуск разработки (только БД):
```bash
docker compose -f docker-compose.dev.yaml up -d
```

Запуск продакшена (приложение + БД):
```bash
docker compose -f docker-compose.prod.yaml up -d --build
```

Остановка:
```bash
docker compose -f docker-compose.prod.yaml down
```

# .env
В файле .env лежат переменные, при запуске на локальной машине при разработке нужно
указывать путь для сохранения данных соответствующий системе