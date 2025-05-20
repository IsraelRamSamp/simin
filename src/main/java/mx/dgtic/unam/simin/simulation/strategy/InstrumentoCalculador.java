package mx.dgtic.unam.simin.simulation.strategy;

import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.Instrumento;

import java.math.BigDecimal;

public interface InstrumentoCalculador {

    /**
     * Simula el comportamiento financiero de un instrumento con base en un monto y un plazo en días.
     *
     * @param instrumento Instrumento financiero a simular
     * @param monto Monto en MXN a invertir
     * @param dias Número de días del plazo
     * @return Resultado de la simulación (intereses, ISR, rendimiento, detalles, etc.)
     */
    SimulacionDto calcular(Instrumento instrumento, BigDecimal monto, long dias);
}