package mx.dgtic.unam.simin.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedRequest(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "La petición está mal formada, por favor revisa");
        body.put("timestamp", LocalDateTime.now());
        body.put("ruta", request.getRequestURI());
        body.put("status", 400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Error de validación");
        body.put("timestamp", LocalDateTime.now());
        body.put("ruta", request.getRequestURI());

        Map<String, String> errores = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        body.put("errores", errores);
        body.put("status", 400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNotFound(
            UsuarioNotFoundException ex,
            HttpServletRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        body.put("ruta", request.getRequestURI());
        body.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
}
