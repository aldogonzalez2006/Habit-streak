
flowchart TD
    A["Crear hábito"] --> B["Crear registro en habits"]
    B --> C["Crear configuración inicial"]
    C --> D{"¿Tiene días específicos?"}
    D -->|Sí| E["Crear registros en habit_schedules"]
    D -->|No| F["No requiere horarios"]
    E --> G["Guardar transacción"]
    F --> G
    G --> H["Hábito disponible"]
