package upeu.edu.pe.autoandes.Exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import upeu.edu.pe.autoandes.Dto.ErrorResponseDTO;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> validation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("Validación rechazada en {}: {}", request.getRequestURI(), campos);
        return response(HttpStatus.BAD_REQUEST, "Datos inválidos", request, campos);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponseDTO> methodValidation(HandlerMethodValidationException ex,
            HttpServletRequest request) {
        log.warn("Parámetros inválidos en {}: {}", request.getRequestURI(), ex.getMessage());
        return response(HttpStatus.BAD_REQUEST, "Parámetros inválidos", request, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> typeMismatch(MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        log.warn("Parámetro {} inválido en {}", ex.getName(), request.getRequestURI());
        return response(HttpStatus.BAD_REQUEST, "El parámetro " + ex.getName() + " tiene un formato inválido",
                request, Map.of(ex.getName(), "Revisa el formato del valor"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> unreadable(HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        log.warn("Cuerpo de solicitud inválido en {}", request.getRequestURI());
        return response(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es válido", request, null);
    }

    @ExceptionHandler(AutenticacionException.class)
    public ResponseEntity<ErrorResponseDTO> authentication(AutenticacionException ex,
            HttpServletRequest request) {
        return response(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", request, null);
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ErrorResponseDTO> invalidRequest(SolicitudInvalidaException ex,
            HttpServletRequest request) {
        log.warn("Solicitud rechazada en {}: {}", request.getRequestURI(), ex.getMessage());
        return response(HttpStatus.BAD_REQUEST, ex.getMessage(), request, ex.getCampos());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> notFound(RecursoNoEncontradoException ex,
            HttpServletRequest request) {
        log.warn("Recurso no encontrado en {}: {}", request.getRequestURI(), ex.getMessage());
        return response(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponseDTO> conflict(ReglaNegocioException ex,
            HttpServletRequest request) {
        log.warn("Regla de negocio rechazada en {}: {}", request.getRequestURI(), ex.getMessage());
        return response(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> integrity(DataIntegrityViolationException ex,
            HttpServletRequest request) {
        log.warn("Restricción de base de datos rechazada en {}", request.getRequestURI());
        return response(HttpStatus.CONFLICT, "El registro entra en conflicto con datos existentes", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> unexpected(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en {}", request.getRequestURI(), ex);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request, null);
    }

    private ResponseEntity<ErrorResponseDTO> response(HttpStatus status, String mensaje,
            HttpServletRequest request, Map<String, String> campos) {
        ErrorResponseDTO body = new ErrorResponseDTO(LocalDateTime.now(), status.value(),
                status.getReasonPhrase(), mensaje, request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(body);
    }
}
