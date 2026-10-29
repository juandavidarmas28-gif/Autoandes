package upeu.edu.pe.autoandes.Security;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import upeu.edu.pe.autoandes.Entity.Rol;

public final class PermisosPorRol {
    private static final Set<Permiso> TODOS = EnumSet.allOf(Permiso.class);

    private PermisosPorRol() {}

    public static Set<Permiso> obtener(Set<Rol> roles) {
        EnumSet<Permiso> permisos = EnumSet.noneOf(Permiso.class);
        for (Rol rol : roles) {
            switch (rol.getNombre()) {
                case "ADMIN" -> permisos.addAll(TODOS);
                case "ASESOR" -> permisos.addAll(EnumSet.of(Permiso.SERVICIO_LEER,
                        Permiso.VEHICULO_LEER, Permiso.VEHICULO_ESCRIBIR,
                        Permiso.ORDEN_LEER, Permiso.ORDEN_REGISTRAR));
                case "JEFE_TALLER" -> permisos.addAll(EnumSet.of(Permiso.SERVICIO_LEER,
                        Permiso.SERVICIO_ESCRIBIR, Permiso.VEHICULO_LEER,
                        Permiso.ORDEN_LEER, Permiso.ORDEN_ANULAR, Permiso.REPORTE_VER));
                default -> { }
            }
        }
        return permisos;
    }

    public static Set<String> comoNombres(Set<Rol> roles) {
        return roles.stream().map(Rol::getNombre).collect(Collectors.toUnmodifiableSet());
    }
}
