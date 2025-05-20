package mx.dgtic.unam.simin.simulation.strategy;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.util.SimulacionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ReportoCalculador implements InstrumentoCalculador {

    private static final BigDecimal TASA_ISR = new BigDecimal("0.057241");

    @Override
    public SimulacionDto calcular(Instrumento instrumento, BigDecimal monto, long dias) {
        SimulacionDto simulacion = new SimulacionDto();
        List<DetalleSimulacionDto> detalles = new ArrayList<>();

        BigDecimal tasa = instrumento.getTasaInteres()
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        BigDecimal interes = monto.multiply(tasa)
                .multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal isr = interes.multiply(TASA_ISR).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinal = monto.add(interes).subtract(isr).setScale(2, RoundingMode.HALF_UP);

        DetalleSimulacionDto detalle = new DetalleSimulacionDto();
        detalle.setNombreInstrumento(instrumento.getNombre());
        detalle.setTipoInstrumento("REPORTO");
        detalle.setTasaBruta(instrumento.getTasaInteres());
        detalle.setTitulos(1); // se asume uno por monto total
        detalle.setInversion(monto);
        detalle.setInteres(interes);
        detalle.setInversionBonddia(BigDecimal.ZERO);
        detalle.setInteresBonddia(BigDecimal.ZERO);
        detalle.setRemanente(BigDecimal.ZERO);
        detalle.setValorFinal(valorFinal);
        detalle.setMontoInvertido(monto);
        detalle.setIsrCalculado(isr);
        detalle.setPlazoTexto(SimulacionUtils.formatearPlazoTexto(dias));

        detalles.add(detalle);

        simulacion.setInteresBruto(interes);
        simulacion.setIsr(isr);
        simulacion.setValorFinal(valorFinal);
        simulacion.setRendimiento(valorFinal.subtract(monto));
        simulacion.setRemanente(BigDecimal.ZERO);
        simulacion.setDetalles(detalles);

        return simulacion;
    }
}