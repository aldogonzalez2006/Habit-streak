
-- =========================================================
-- Habit Tracker
-- V1: Initial database schema
-- PostgreSQL 17
-- =========================================================

-- Permite utilizar BIGINT en restricciones de exclusion GiST.
CREATE EXTENSION IF NOT EXISTS btree_gist;


-- =========================================================
-- 1. USERS
-- =========================================================

CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    email VARCHAR(255) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ck_users_email_not_blank
        CHECK (
            LENGTH(TRIM(email)) > 0
            AND email = TRIM(email)
        )
);

-- Correo único sin distinguir mayúsculas y minúsculas.
CREATE UNIQUE INDEX uq_users_email_lower
    ON users (LOWER(email));


-- =========================================================
-- 2. HABITS
-- =========================================================

CREATE TABLE habits (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    user_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,
    description TEXT,
    color VARCHAR(20),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_habits_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_habits_name_not_blank
        CHECK (LENGTH(TRIM(name)) > 0)
);

CREATE INDEX idx_habits_user_id
    ON habits (user_id);


-- =========================================================
-- 3. HABIT CONFIGURATIONS
-- =========================================================

CREATE TABLE habit_configurations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    habit_id BIGINT NOT NULL,

    tracking_type VARCHAR(20) NOT NULL,
    frequency_type VARCHAR(20) NOT NULL,

    target_value NUMERIC(10, 2),
    unit VARCHAR(30),

    weekly_target_count SMALLINT,

    effective_from DATE NOT NULL,
    effective_until DATE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_configurations_habit
        FOREIGN KEY (habit_id)
        REFERENCES habits (id)
        ON DELETE RESTRICT,

    -- Necesaria para la FK compuesta de habit_progress.
    CONSTRAINT uq_configurations_id_habit
        UNIQUE (id, habit_id),

    CONSTRAINT ck_configurations_tracking_type
        CHECK (
            tracking_type IN ('BOOLEAN', 'QUANTITY')
        ),

    CONSTRAINT ck_configurations_frequency_type
        CHECK (
            frequency_type IN (
                'DAILY',
                'SPECIFIC_DAYS',
                'WEEKLY_TARGET'
            )
        ),

    CONSTRAINT ck_configurations_target
        CHECK (
            (
                tracking_type = 'BOOLEAN'
                AND target_value IS NULL
                AND unit IS NULL
            )
            OR
            (
                tracking_type = 'QUANTITY'
                AND target_value IS NOT NULL
                AND target_value > 0
                AND target_value <> 'NaN'::numeric
                AND unit IS NOT NULL
                AND LENGTH(TRIM(unit)) > 0
            )
        ),

    CONSTRAINT ck_configurations_weekly_target
        CHECK (
            (
                frequency_type = 'WEEKLY_TARGET'
                AND weekly_target_count IS NOT NULL
                AND weekly_target_count BETWEEN 1 AND 7
            )
            OR
            (
                frequency_type <> 'WEEKLY_TARGET'
                AND weekly_target_count IS NULL
            )
        ),

    CONSTRAINT ck_configurations_effective_dates
        CHECK (
            effective_until IS NULL
            OR effective_until > effective_from
        ),

    -- Impide configuraciones con periodos superpuestos.
    -- Los intervalos utilizan inicio incluido y fin excluido.
    CONSTRAINT ex_configurations_no_overlap
        EXCLUDE USING GIST (
            habit_id WITH =,
            daterange(effective_from, effective_until, '[)') WITH &&
        )
);

CREATE INDEX idx_configurations_habit_id
    ON habit_configurations (habit_id);


-- =========================================================
-- 4. HABIT SCHEDULES
-- =========================================================

CREATE TABLE habit_schedules (
    configuration_id BIGINT NOT NULL,

    day_of_week SMALLINT NOT NULL,

    CONSTRAINT pk_habit_schedules
        PRIMARY KEY (configuration_id, day_of_week),

    CONSTRAINT fk_schedules_configuration
        FOREIGN KEY (configuration_id)
        REFERENCES habit_configurations (id)
        ON DELETE RESTRICT,

    CONSTRAINT ck_schedules_day_of_week
        CHECK (day_of_week BETWEEN 1 AND 7)
);


-- =========================================================
-- 5. HABIT PROGRESS
-- =========================================================

CREATE TABLE habit_progress (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    habit_id BIGINT NOT NULL,
    configuration_id BIGINT NOT NULL,

    progress_date DATE NOT NULL,
    progress_value NUMERIC(10, 2) NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_progress_configuration
        FOREIGN KEY (configuration_id, habit_id)
        REFERENCES habit_configurations (id, habit_id)
        ON DELETE RESTRICT,

    -- Un registro por hábito y fecha.
    CONSTRAINT uq_progress_habit_date
        UNIQUE (habit_id, progress_date),

    CONSTRAINT ck_progress_value_non_negative
        CHECK (
        progress_value >= 0
        AND progress_value <> 'NaN'::numeric
)
);


-- =========================================================
-- 6. AUTOMATIC UPDATED_AT
-- =========================================================

CREATE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_habits_updated_at
    BEFORE UPDATE ON habits
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at();

CREATE TRIGGER trg_habit_progress_updated_at
    BEFORE UPDATE ON habit_progress
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at();
