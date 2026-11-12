
erDiagram
    HABIT ||--o{ HABIT_SCHEDULE : "has"
    HABIT ||--o{ HABIT_PROGRESS : "records"

    HABIT {
        bigint id PK
        varchar name
        varchar description
        varchar tracking_type
        varchar frequency_type
        numeric target_value
        varchar unit
        smallint weekly_target_count
        varchar color
        boolean is_active
        timestamptz created_at
        timestamptz updated_at
    }

    HABIT_SCHEDULE {
        bigint habit_id PK, FK
        smallint day_of_week PK
    }

    HABIT_PROGRESS {
        bigint id PK
        bigint habit_id FK
        date progress_date
        numeric progress_value
        numeric target_snapshot
        varchar unit_snapshot
        timestamptz created_at
        timestamptz updated_at
    }
