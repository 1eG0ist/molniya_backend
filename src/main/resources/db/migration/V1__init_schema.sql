-- =========================
-- Роли
-- =========================
CREATE TABLE roles (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(50) UNIQUE NOT NULL -- PLAYER, TRAINER, PARENT, ADMIN
);

INSERT INTO roles (name) 
VALUES ('PLAYER'), 
       ('TRAINER'), 
       ('PARENT'), 
       ('ADMIN');

-- =========================
-- Пользователи
-- =========================
CREATE TABLE users (
        id          BIGSERIAL PRIMARY KEY,
        username    VARCHAR(100) UNIQUE NOT NULL,
        full_name   VARCHAR(255),
        role_id     BIGINT NOT NULL REFERENCES roles(id),
        birthday    DATE,
        photo_url   TEXT,
        coins       INT DEFAULT 0 CHECK (coins >= 0),
        created_at  TIMESTAMP DEFAULT now(),
        updated_at  TIMESTAMP DEFAULT now()
);

-- =========================
-- Чувствительные данные (только пароль)
-- =========================
CREATE TABLE user_sensitive_data (
        id          BIGSERIAL PRIMARY KEY,
        user_id     BIGINT UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        password    VARCHAR(255) NOT NULL
);

-- =========================
-- Лог транзакций внутренней валюты
-- =========================
CREATE TABLE coin_transactions (
        id              BIGSERIAL PRIMARY KEY,
        user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        amount          INT NOT NULL,
        description     VARCHAR(255),
        created_by_id   BIGINT REFERENCES users(id), -- кто начислил/списал
        created_at      TIMESTAMP DEFAULT now()
);

-- =========================
-- Игрок
-- =========================
CREATE TABLE players (
        id          BIGSERIAL PRIMARY KEY,
        user_id     BIGINT UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        position    VARCHAR(50),
        number      INT
);

-- =========================
-- Сезоны
-- =========================
CREATE TABLE seasons (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(100) NOT NULL,
        start_date  DATE,
        end_date    DATE
);

-- =========================
-- Статистика игрока по сезонам
-- =========================
CREATE TABLE player_stats (
        id          BIGSERIAL PRIMARY KEY,
        player_id   BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
        season_id   BIGINT NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
        goals       INT DEFAULT 0,
        assists     INT DEFAULT 0,
        dribbles    INT DEFAULT 0,
        distance    NUMERIC(6,2), -- км
        max_speed   NUMERIC(5,2), -- км/ч
        created_at  TIMESTAMP DEFAULT now()
);

-- =========================
-- Замеры дистанций
-- =========================
CREATE TABLE player_distances (
        id          BIGSERIAL PRIMARY KEY,
        user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        distance    NUMERIC(6,2) NOT NULL, -- км
        time        INTERVAL NOT NULL,
        measured_at TIMESTAMP DEFAULT now()
);

-- =========================
-- Локации
-- =========================
CREATE TABLE locations (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(255) NOT NULL,
        address     VARCHAR(255)
);

-- =========================
-- Тренировки
-- =========================
CREATE TABLE trainings (
        id          BIGSERIAL PRIMARY KEY,
        title       VARCHAR(255) NOT NULL,
        start_time  TIMESTAMP NOT NULL,
        end_time    TIMESTAMP,
        location_id BIGINT REFERENCES locations(id),
        created_at  TIMESTAMP DEFAULT now()
);

-- =========================
-- Дневник: оценки тренера игроку
-- =========================
CREATE TABLE player_evaluations (
        id          BIGSERIAL PRIMARY KEY,
        player_id   BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
        trainer_id  BIGINT NOT NULL REFERENCES users(id), -- тренер
        training_id BIGINT REFERENCES trainings(id),
        score       INT NOT NULL CHECK (score BETWEEN 1 AND 100),
        note        TEXT,
        created_at  TIMESTAMP DEFAULT now(),
        UNIQUE (player_id, trainer_id, training_id) -- один тренер — одна оценка на тренировку
);

-- =========================
-- Заметки игрока о тренировках
-- =========================
CREATE TABLE player_notes (
        id          BIGSERIAL PRIMARY KEY,
        player_id   BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
        training_id BIGINT REFERENCES trainings(id), -- опционально
        note        TEXT NOT NULL,
        created_at  TIMESTAMP DEFAULT now()
);

-- =========================
-- Команды
-- =========================
CREATE TABLE teams (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(255) NOT NULL,
        created_at  TIMESTAMP DEFAULT now()
);

-- =========================
-- Игроки в командах (many-to-many)
-- =========================
CREATE TABLE players_teams (
        id          BIGSERIAL PRIMARY KEY,
        player_id   BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
        team_id     BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
        joined_at   TIMESTAMP DEFAULT now(),
        UNIQUE (player_id, team_id)
);

-- =========================
-- Турниры
-- =========================
CREATE TABLE tournaments (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(255) NOT NULL,
        season_id   BIGINT REFERENCES seasons(id) ON DELETE SET NULL,
        created_at  TIMESTAMP DEFAULT now()
);

-- =========================
-- Матчи
-- =========================
CREATE TABLE matches (
        id            BIGSERIAL PRIMARY KEY,
        home_team_id  BIGINT REFERENCES teams(id) ON DELETE SET NULL,
        away_team_id  BIGINT REFERENCES teams(id) ON DELETE SET NULL,
        match_date    TIMESTAMP NOT NULL,
        score_home    INT,
        score_away    INT,
        tournament_id BIGINT REFERENCES tournaments(id) ON DELETE SET NULL
);

-- =========================
-- Турнирная таблица
-- =========================
CREATE TABLE tournament_table (
        id            BIGSERIAL PRIMARY KEY,
        tournament_id BIGINT NOT NULL REFERENCES tournaments(id) ON DELETE CASCADE,
        team_id       BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
        played        INT DEFAULT 0,
        wins          INT DEFAULT 0,
        draws         INT DEFAULT 0,
        losses        INT DEFAULT 0,
        points        INT DEFAULT 0
);

-- =========================
-- Напоминания
-- =========================
CREATE TABLE reminders (
        id          BIGSERIAL PRIMARY KEY,
        user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        message     VARCHAR(255) NOT NULL,
        remind_at   TIMESTAMP NOT NULL,
        created_at  TIMESTAMP DEFAULT now()
);

-- =========================
-- Цели игрока
-- =========================
CREATE TABLE goals (
        id            BIGSERIAL PRIMARY KEY,
        player_id     BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
        description   VARCHAR(1023) NOT NULL,
        target_date   DATE,
        is_completed  BOOLEAN DEFAULT FALSE
);

-- =========================
-- Магазин
-- =========================
CREATE TABLE products (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(255) NOT NULL,
        description TEXT,
        price       INT NOT NULL CHECK (price >= 0),
        stock       INT DEFAULT 0 CHECK (stock >= 0),
        file_path   TEXT,
        created_at  TIMESTAMP DEFAULT now()
);

-- =========================
-- Покупки
-- =========================
CREATE TABLE purchases (
        id           BIGSERIAL PRIMARY KEY,
        user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        product_id   BIGINT NOT NULL REFERENCES products(id),
        quantity     INT NOT NULL CHECK (quantity > 0),
        total_price  INT NOT NULL CHECK (total_price >= 0),
        purchased_at TIMESTAMP DEFAULT now()
);
