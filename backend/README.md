# AutoAndes API

Backend REST del caso AutoAndes: Spring Boot, Oracle, Flyway, JPA y JWT.

## Requisitos y arranque

Configura en PowerShell los datos de tu instancia Oracle y una clave JWT de al menos 32 bytes. No subas esas variables al repositorio.

```powershell
$env:DB_URL = 'jdbc:oracle:thin:@localhost:1521/FREEPDB1'
$env:DB_USERNAME = 'tu_usuario_oracle'
$env:DB_PASSWORD = 'tu_clave_oracle'
$env:JWT_SECRET = 'reemplaza-por-una-clave-aleatoria-de-32-bytes-o-mas'
.\mvnw.cmd spring-boot:run
```

Flyway crea el esquema y los datos de muestra. Hibernate valida el esquema al arrancar. Al iniciar, la aplicación crea los roles y usuarios de demostración con contraseñas cifradas mediante BCrypt.

## Usuarios de demostración

| Usuario | Contraseña | Rol | Estado |
|---|---|---|---|
| admin | Auto2026* | ADMIN | Activo |
| asesor | Auto2026* | ASESOR | Activo |
| jefe | Auto2026* | JEFE_TALLER | Activo |
| inactivo | Auto2026* | ASESOR | Inactivo |

## API

Las rutas usan el prefijo `/api/v1`. La documentación Swagger está en `/swagger-ui.html`.

- `POST /auth/login`, `GET /auth/me`
- `GET /clientes`
- `GET, POST /servicios`; `GET, PUT, DELETE /servicios/{id}`; `PATCH /servicios/{id}/estado`
- `GET, POST /vehiculos`; `GET, PUT, DELETE /vehiculos/{id}`
- `POST /ordenes`; `GET /ordenes?placa&estado&desde&hasta`; `GET /ordenes/{id}`; `PATCH /ordenes/{id}/anular`
- `GET /reportes/ingresos-por-servicio?desde&hasta`

Los endpoints distintos del login requieren `Authorization: Bearer <token>`. El origen permitido para CORS es `http://localhost:4200`.
