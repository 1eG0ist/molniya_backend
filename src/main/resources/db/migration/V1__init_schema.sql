-- =========================
-- Роли
-- =========================
CREATE TABLE roles (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(50) UNIQUE NOT NULL -- USER, PLAYER, TRAINER, ADMIN
);

INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_PLAYER'),
       ('ROLE_TRAINER'),
       ('ROLE_ADMIN');

-- =========================
-- Пользователи
-- =========================
CREATE TABLE users (
        id          BIGSERIAL PRIMARY KEY,
        username    VARCHAR(100) UNIQUE NOT NULL,
        full_name   VARCHAR(255),
        birthday    DATE,
        photo_url   TEXT,
        phone       VARCHAR(50) UNIQUE NOT NULL ,
        email       VARCHAR(100) UNIQUE,
        coins       REAL DEFAULT 0 CHECK (coins >= 0),
        created_at  TIMESTAMP WITH TIME ZONE DEFAULT now(),
        updated_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE user_roles (
        id          BIGSERIAL PRIMARY KEY,
        user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        role_id     BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE
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
        amount          REAL NOT NULL,
        description     VARCHAR(255),
        created_by_id   BIGINT REFERENCES users(id), -- кто начислил/списал
        created_at      TIMESTAMP WITH TIME ZONE DEFAULT now()
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
        passes      INT DEFAULT 0,
        sprints     INT DEFAULT 0,
        created_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- =========================
-- Замеры дистанций
-- =========================
CREATE TABLE player_distances (
        id          BIGSERIAL PRIMARY KEY,
        user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        distance    REAL NOT NULL, -- meters
        time        REAL NOT NULL,
        measured_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- =========================
-- Тренировки
-- =========================
CREATE TABLE trainings (
        id          BIGSERIAL PRIMARY KEY,
        title       VARCHAR(255) NOT NULL,
        description VARCHAR(511) NOT NULL,
        trainer_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        start_time  TIMESTAMP WITH TIME ZONE NOT NULL,
        end_time    TIMESTAMP WITH TIME ZONE,
        created_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- =========================
-- Тренировки связанные с игроком
-- =========================
CREATE TABLE player_trainings (
        id          BIGSERIAL PRIMARY KEY,
        training_id BIGINT NOT NULL REFERENCES trainings(id) ON DELETE CASCADE,
        player_id   BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
        note        VARCHAR(1023)
);

-- =========================
-- Дневник: оценки тренера игроку
-- =========================
CREATE TABLE player_evaluations (
        id          BIGSERIAL PRIMARY KEY,
        player_id   BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
        trainer_id  BIGINT NOT NULL REFERENCES users(id),
        training_id BIGINT REFERENCES trainings(id),
        score       INT NOT NULL CHECK (score BETWEEN 1 AND 100),
        note        TEXT,
        created_at  TIMESTAMP WITH TIME ZONE DEFAULT now(),
        updated_at  TIMESTAMP WITH TIME ZONE DEFAULT now(),
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
        created_at  TIMESTAMP WITH TIME ZONE DEFAULT now(),
        updated_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- =========================
-- Команды
-- =========================
CREATE TABLE teams (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(255) NOT NULL,
        created_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- =========================
-- Игроки в командах (many-to-many)
-- =========================
CREATE TABLE players_teams (
        id          BIGSERIAL PRIMARY KEY,
        player_id   BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
        team_id     BIGINT NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
        joined_at   TIMESTAMP WITH TIME ZONE DEFAULT now(),
        UNIQUE (player_id, team_id)
);

-- =========================
-- Турниры
-- =========================
CREATE TABLE tournaments (
        id                      BIGSERIAL PRIMARY KEY,
        name                    VARCHAR(255) NOT NULL,
        season_id               BIGINT REFERENCES seasons(id) ON DELETE SET NULL,
        tournament_start_date   TIMESTAMP WITH TIME ZONE DEFAULT now(),
        tournament_end_date     TIMESTAMP WITH TIME ZONE DEFAULT now(),
        created_at              TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- =========================
-- Матчи
-- =========================
CREATE TABLE matches (
        id            BIGSERIAL PRIMARY KEY,
        home_team_id  BIGINT REFERENCES teams(id) ON DELETE SET NULL,
        away_team_id  BIGINT REFERENCES teams(id) ON DELETE SET NULL,
        match_date    TIMESTAMP WITH TIME ZONE NOT NULL,
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
        remind_at   TIMESTAMP WITH TIME ZONE NOT NULL,
        created_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
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
-- Магазин (только под админом добавление)
-- =========================
CREATE TABLE products (
        id          BIGSERIAL PRIMARY KEY,
        name        VARCHAR(127) NOT NULL,
        description VARCHAR(511),
        price       REAL NOT NULL CHECK (price >= 0),
        stock       INT DEFAULT 0 CHECK (stock >= 0),
        image_path  TEXT,
        created_at  TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- =========================
-- Покупки
-- =========================
CREATE TABLE purchases (
        id           BIGSERIAL PRIMARY KEY,
        user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        product_id   BIGINT NOT NULL REFERENCES products(id),
        quantity     INT NOT NULL CHECK (quantity > 0),
        total_price  REAL NOT NULL CHECK (total_price >= 0),
        purchased_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE refresh_tokens (
        id           BIGSERIAL PRIMARY KEY,
        user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        token        VARCHAR(255) NOT NULL,
        expiry_date  TIMESTAMP WITH TIME ZONE NOT NULL,
        revoked      BOOLEAN NOT NULL DEFAULT FALSE
);