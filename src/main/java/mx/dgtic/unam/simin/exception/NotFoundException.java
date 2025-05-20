package mx.dgtic.unam.simin.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("Recurso no encontrado");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
