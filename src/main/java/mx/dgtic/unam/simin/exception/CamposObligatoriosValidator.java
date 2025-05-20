package mx.dgtic.unam.simin.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mx.dgtic.unam.simin.dto.InstrumentoDto;

public class CamposObligatoriosValidator implements ConstraintValidator<ValidarCamposObligatorios, InstrumentoDto> {

    @Override
    public boolean isValid(InstrumentoDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true; // Manejar null según sea necesario
        }

        String tipoInstrumento = dto.getTipoInstrumentoDescripcion();
        if ("BONO".equalsIgnoreCase(tipoInstrumento) || "UDIBONOS".equalsIgnoreCase(tipoInstrumento)) {
            boolean valido = dto.getFechaEmision() != null && dto.getFechaVencimiento() != null && dto.getTasaCupon() != null;
            if (!valido) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Los campos fecha de emisión, fecha de vencimiento y tasa cupón son obligatorios para BONO y UDIBONOS.")
                        .addConstraintViolation();
            }
            return valido;
        }
        return true;
    }
}