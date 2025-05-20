package mx.dgtic.unam.simin.simulation.strategy;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.util.SimulacionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BonddiaCalculador implements InstrumentoCalculador {

    private static final BigDecimal TASA_ISR = new BigDecimal("0.057241");
    private static final BigDecimal TASA_BONDDIA = new BigDecimal("8.93").divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
    private static final BigDecimal PRECIO_MERCADO_BONDDIA = new BigDecimal("2.1712");

    @Override
    public SimulacionDto calcular(Instrumento instrumento, BigDecimal monto, long dias) {
        SimulacionDto simulacion = new SimulacionDto();
        DetalleSimulacionDto detalle = new DetalleSimulacionDto();

        // Cálculo de títulos posibles
        int titulos = monto.divide(PRECIO_MERCADO_BONDDIA, 0, RoundingMode.DOWN).intValue();
        BigDecimal inversionReal = PRECIO_MERCADO_BONDDIA.multiply(BigDecimal.valueOf(titulos)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal remanente = monto.subtract(inversionReal).setScale(2, RoundingMode.HALF_UP);

        // Cálculo del interés generado
        BigDecimal interes = inversionReal.multiply(TASA_BONDDIA)
                .multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal isr = interes.multiply(TASA_ISR).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinal = inversionReal.add(interes).subtract(isr).add(remanente).setScale(2, RoundingMode.HALF_UP);

        // Poblado del detalle
        detalle.setNombreInstrumento("BONDDIA");
        detalle.setTipoInstrumento("BONDDIA");
        detalle.setTasaBruta(TASA_BONDDIA.multiply(BigDecimal.valueOf(100)));
        detalle.setTitulos(titulos);
        detalle.setTitulosBonddia(titulos); // mismo valor porque es BONDDIA
        detalle.setInversion(inversionReal);
        detalle.setInteres(interes);
        detalle.setIsrCalculado(isr);
        detalle.setRemanente(remanente);
        detalle.setInversionBonddia(BigDecimal.ZERO); // no aplica inversión en otro instrumento
        detalle.setInteresBonddia(BigDecimal.ZERO);   // no aplica interés externo
        detalle.setValorFinal(valorFinal);
        detalle.setMontoInvertido(monto);
        detalle.setPlazoTexto(SimulacionUtils.formatearPlazoTexto(dias));

        // Asignación al DTO global
        simulacion.setInteresBruto(interes);
        simulacion.setIsr(isr);
        simulacion.setValorFinal(valorFinal);
        simulacion.setRendimiento(valorFinal.subtract(monto).setScale(2, RoundingMode.HALF_UP));
        simulacion.setRemanente(remanente);
        simulacion.setDetalles(List.of(detalle));

        return simulacion;
    }
}