package mx.dgtic.unam.simin.simulation.strategy;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.util.SimulacionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class UdibonosCalculador implements InstrumentoCalculador {

    private static final BigDecimal TASA_ISR = new BigDecimal("0.057241");
    private static final BigDecimal INFLACION_ESTIMADA = new BigDecimal("0.0393"); // 3.93% anual
    private static final BigDecimal TASA_BONDDIA = new BigDecimal("0.0893");        // 8.93% anual
    private static final BigDecimal PRECIO_BONDDIA = new BigDecimal("2.17");        // precio real por título BONDDIA
    private static final long PLAZO_BONDDIA_DIAS = 28;

    @Override
    public SimulacionDto calcular(Instrumento instrumento, BigDecimal monto, long dias) {
        SimulacionDto dto = new SimulacionDto();
        List<DetalleSimulacionDto> detalles = new ArrayList<>();

        BigDecimal tasaCupon = instrumento.getTasaCupon().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal valorNominal = instrumento.getValorNominal();

        // 1. Calcular títulos y monto invertido real en UDIBONOS
        int titulos = monto.divide(valorNominal, 0, RoundingMode.DOWN).intValue();
        BigDecimal inversionReal = valorNominal.multiply(BigDecimal.valueOf(titulos)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal remanente = monto.subtract(inversionReal).setScale(2, RoundingMode.HALF_UP);

        // 2. Interés real gravable
        BigDecimal interesReal = valorNominal.multiply(BigDecimal.valueOf(titulos))
                .multiply(tasaCupon)
                .multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        // 3. Ajuste por inflación (informativo, no gravable)
        BigDecimal inflacion = INFLACION_ESTIMADA.multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);
        BigDecimal ajusteCapital = inversionReal.multiply(inflacion).setScale(2, RoundingMode.HALF_UP);

        // 4. ISR solo sobre el interés real
        BigDecimal isr = interesReal.multiply(TASA_ISR).setScale(2, RoundingMode.HALF_UP);

        // 5. Inversión de remanente en BONDDIA (usando 28 días fijos)
        int titulosBonddia = remanente.divide(PRECIO_BONDDIA, 0, RoundingMode.DOWN).intValue();
        BigDecimal inversionBonddia = PRECIO_BONDDIA.multiply(BigDecimal.valueOf(titulosBonddia)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interesBonddia = inversionBonddia.multiply(TASA_BONDDIA)
                .multiply(BigDecimal.valueOf(PLAZO_BONDDIA_DIAS))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal remanenteFinal = remanente.subtract(inversionBonddia).setScale(2, RoundingMode.HALF_UP);

        // 6. Valor final (sin ajuste de inflación)
        BigDecimal valorFinal = monto.add(interesReal).add(interesBonddia).subtract(isr).setScale(2, RoundingMode.HALF_UP);

        // 7. Detalle de simulación
        DetalleSimulacionDto detalle = new DetalleSimulacionDto();
        detalle.setNombreInstrumento(instrumento.getNombre());
        detalle.setTipoInstrumento("UDIBONOS");
        detalle.setTitulos(titulos);
        detalle.setTasaBruta(instrumento.getTasaCupon());
        detalle.setInversion(inversionReal);
        detalle.setInteres(interesReal);
        detalle.setInteresBonddia(interesBonddia);
        detalle.setInversionBonddia(inversionBonddia);
        detalle.setRemanente(remanenteFinal);
        detalle.setValorFinal(valorFinal);
        detalle.setTitulosBonddia(titulosBonddia);
        detalle.setMontoInvertido(monto);
        detalle.setIsrCalculado(isr);
        detalle.setPlazoTexto(SimulacionUtils.formatearPlazoTexto(dias));

        detalles.add(detalle);

        // 8. Llenar DTO final
        dto.setDetalles(detalles);
        dto.setInteresBruto(interesReal.add(interesBonddia));
        dto.setIsr(isr);
        dto.setValorFinal(valorFinal);
        dto.setRendimiento(valorFinal.subtract(monto));
        dto.setRemanente(remanenteFinal);

        return dto;
    }
}