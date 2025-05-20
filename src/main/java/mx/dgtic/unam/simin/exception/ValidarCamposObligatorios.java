package mx.dgtic.unam.simin.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CamposObligatoriosValidator.class)
public @interface ValidarCamposObligatorios {
    String message() default "Campos obligatorios faltantes para el tipo de instrumento.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}