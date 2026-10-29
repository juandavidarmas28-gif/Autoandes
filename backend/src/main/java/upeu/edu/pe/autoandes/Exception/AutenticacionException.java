package upeu.edu.pe.autoandes.Exception;

public class AutenticacionException extends RuntimeException {
    public AutenticacionException() {
        super("Usuario o contraseña incorrectos");
    }
}
