package upeu.edu.pe.autoandes.Exception;

import java.util.Map;

public class SolicitudInvalidaException extends RuntimeException {
    private final Map<String, String> campos;

    public SolicitudInvalidaException(String mensaje, Map<String, String> campos) {
        super(mensaje);
        this.campos = Map.copyOf(campos);
    }

    public Map<String, String> getCampos() {
        return campos;
    }
}
