# Habit Tracker — Comandos de desarrollo

## 1. PostgreSQL con Docker Compose

Ejecutar estos comandos desde la raíz del repositorio, donde está `docker-compose.yml`.

### Encender PostgreSQL

```bash
docker compose up -d
```

Inicia los servicios en segundo plano.

### Apagar PostgreSQL

```bash
docker compose down
```

Detiene y elimina los contenedores, pero conserva los volúmenes y los datos.

### Reiniciar PostgreSQL

```bash
docker compose restart postgres
```

Reinicia el servicio sin eliminar los datos.

### Verificar el estado

```bash
docker compose ps
```

Muestra los contenedores y su estado.

### Consultar los registros

```bash
docker compose logs postgres
```

Mostrar los registros en tiempo real:

```bash
docker compose logs -f postgres
```

Salir de la visualización con `Ctrl + C`.

### Verificar que PostgreSQL responde

```bash
docker exec -it habit-tracker-postgres pg_isready -U habit_user -d habit_tracker
```

### Eliminar también los datos persistidos

```bash
docker compose down -v
```

**PELIGRO:** elimina los volúmenes asociados al proyecto y puede borrar permanentemente la base de datos. No utilizar como comando habitual.

---

## 2. Conectarse a PostgreSQL

### Abrir la consola interactiva

```bash
docker exec -it habit-tracker-postgres psql -U habit_user -d habit_tracker
```

### Ejecutar una consulta sin abrir la consola

```bash
docker exec -it habit-tracker-postgres psql -U habit_user -d habit_tracker -c "SELECT current_database(), current_user;"
```

### Comandos útiles dentro de psql

| Comando | Función |
|---|---|
| `\l` | Listar bases de datos. |
| `\dt` | Listar tablas. |
| `\d nombre_tabla` | Ver estructura de una tabla. |
| `\du` | Listar usuarios y roles. |
| `\conninfo` | Ver información de conexión. |
| `\q` | Salir de psql. |

Consultas SQL:

```sql
SELECT current_database();
SELECT current_user;
SELECT version();
```

---

## 3. Datos de conexión del entorno local

- Motor: PostgreSQL 17
- Host: `localhost`
- Puerto: `5432`
- Base de datos: `habit_tracker`
- Usuario: `habit_user`
- Contraseña actual de desarrollo: `local_dev_password`
- Contenedor: `habit-tracker-postgres`
- Volumen: `habit-streak_postgres_data`
- Zona horaria de la aplicación: `America/Mexico_City`

La contraseña es temporal y exclusiva para desarrollo local. No utilizarla en producción ni compartirla públicamente.

---

## 4. Flujo habitual de trabajo

Cuando comience a desarrollar:

```bash
docker compose up -d
docker compose ps
```

Cuando termine:

```bash
docker compose down
```

Si necesito revisar qué ocurrió:

```bash
docker compose logs postgres
```