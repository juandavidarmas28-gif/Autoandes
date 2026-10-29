# AutoAndes Web

SPA para el backend AutoAndes. Está construida con Angular standalone y consume el API en `http://localhost:8080/api/v1`.

## Requisitos y ejecución

Se requiere Node.js 24.15 o superior dentro de la rama 24 y npm. Desde esta carpeta:

```powershell
npm install
npm start
```

La aplicación queda disponible en `http://localhost:4200`. Antes, inicia el backend AutoAndes y configura la conexión Oracle y `JWT_SECRET`.

## Funcionalidades

- Inicio y cierre de sesión con almacenamiento en `sessionStorage` y vencimiento automático.
- Autorización de rutas y menú por permisos que devuelve el backend.
- CRUD de servicios y vehículos, con clientes cargados desde el API.
- Registro de órdenes con líneas dinámicas, cantidades, subtotales y total estimado.
- Consulta filtrable, detalle y anulación de órdenes.
- Reporte de ingresos por servicio.
- Interceptor Bearer limitado al API; ante 401 cierra la sesión y redirige al login; 403 conserva la sesión.

## Usuarios de demostración

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | Auto2026* | ADMIN |
| asesor | Auto2026* | ASESOR |
| jefe | Auto2026* | JEFE_TALLER |
| inactivo | Auto2026* | ASESOR (inactivo) |
