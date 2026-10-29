# AutoAndes

Proyecto full stack para la gestión de servicios, vehículos y órdenes de un taller.

## Carpetas

- `backend/`: API REST con Spring Boot, Java 21, Maven y Oracle Database.
- `frontend/`: aplicación web con Angular.

## Requisitos

- JDK 21 o posterior.
- Node.js compatible con la versión Angular del frontend.
- Oracle Database con el servicio/PDB configurado.

## Ejecutar el backend (PowerShell)

Desde `backend/`, configura tus credenciales localmente y ejecuta Maven Wrapper:

```powershell
$env:DB_URL = 'jdbc:oracle:thin:@//localhost:1522/FREEPDB1'
$env:DB_USERNAME = 'Autoandes'
$env:DB_PASSWORD = 'tu_clave_oracle'
$env:JWT_SECRET = 'una_clave_aleatoria_de_32_bytes_o_mas'
.\mvnw.cmd spring-boot:run
```

La API está en `http://localhost:8080/api/v1`.

## Ejecutar el frontend

Desde `frontend/`:

```powershell
npm ci
npm start
```

La aplicación se sirve en `http://localhost:4200` y consume la API local en el puerto 8080.

No subas contraseñas, tokens ni archivos `.env` al repositorio. Las credenciales de la base de datos y la clave JWT se leen desde variables de entorno.
