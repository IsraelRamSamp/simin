package mx.dgtic.unam.simin.simulation.strategy;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.util.SimulacionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BonoCalculador implements InstrumentoCalculador {

    private static final BigDecimal TASA_ISR = new BigDecimal("0.0145"); // 1.45%
    private static final BigDecimal PRECIO_BONDDIA = new BigDecimal("2.20");
    private static final BigDecimal TASA_BONDDIA = new BigDecimal("8.93").divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

    @Override
    public SimulacionDto calcular(Instrumento instrumento, BigDecimal monto, long dias) {
        SimulacionDto dto = new SimulacionDto();
        List<DetalleSimulacionDto> detalles = new ArrayList<>();

        if (instrumento.getFechaEmision() == null || instrumento.getFechaVencimiento() == null || instrumento.getTasaCupon() == null) {
            throw new IllegalArgumentException("El bono debe tener fecha de emisión, vencimiento y tasa cupón definida.");
        }

        BigDecimal tasaCupon = instrumento.getTasaCupon().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal valorNominal = instrumento.getValorNominal();
        BigDecimal precioMercado = instrumento.getPrecioMercado();

        long diasTotales = Duration.between(instrumento.getFechaEmision(), instrumento.getFechaVencimiento()).toDays();
        if (diasTotales <= 0) throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a la de emisión.");

        // Compra principal
        int titulos = monto.divide(precioMercado, 0, RoundingMode.DOWN).intValue();
        BigDecimal inversionReal = precioMercado.multiply(BigDecimal.valueOf(titulos)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal remanente = monto.subtract(inversionReal).setScale(2, RoundingMode.HALF_UP);

        int nPagos = (int) (diasTotales / 182.5);
        BigDecimal cupon = valorNominal.multiply(tasaCupon).divide(BigDecimal.valueOf(2), 6, RoundingMode.HALF_UP);
        BigDecimal interesPrincipal = cupon.multiply(BigDecimal.valueOf(nPagos)).multiply(BigDecimal.valueOf(titulos)).setScale(2, RoundingMode.HALF_UP);

        // Inversión en BONDDIA con remanente
        int titulosBonddia = remanente.divide(PRECIO_BONDDIA, 0, RoundingMode.DOWN).intValue();
        BigDecimal inversionBonddia = PRECIO_BONDDIA.multiply(BigDecimal.valueOf(titulosBonddia)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interesBonddia = inversionBonddia.multiply(TASA_BONDDIA)
                .multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        remanente = remanente.subtract(inversionBonddia).setScale(2, RoundingMode.HALF_UP);

        // Totales
        BigDecimal interesTotal = interesPrincipal.add(interesBonddia);
        BigDecimal isr = interesTotal.multiply(TASA_ISR).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinal = inversionReal.add(inversionBonddia).add(interesTotal).subtract(isr).setScale(2, RoundingMode.HALF_UP);

        // Detalle
        DetalleSimulacionDto detalle = new DetalleSimulacionDto();
        detalle.setNombreInstrumento(instrumento.getNombre());
        detalle.setTipoInstrumento(instrumento.getTipoInstrumento().getDescripcion());
        detalle.setTitulos(titulos);
        detalle.setTasaBruta(instrumento.getTasaCupon());
        detalle.setInversion(inversionReal);
        detalle.setInteres(interesPrincipal);
        detalle.setTitulosBonddia(titulosBonddia);
        detalle.setInversionBonddia(inversionBonddia);
        detalle.setInteresBonddia(interesBonddia);
        detalle.setRemanente(remanente);
        detalle.setIsrCalculado(isr);
        detalle.setValorFinal(valorFinal);
        detalle.setMontoInvertido(monto);
        detalle.setPlazoTexto(SimulacionUtils.formatearPlazoTexto(dias));

        detalles.add(detalle);

        // DTO global
        dto.setInteresBruto(interesTotal);
        dto.setIsr(isr);
        dto.setValorFinal(valorFinal);
        dto.setRendimiento(valorFinal.subtract(monto).setScale(2, RoundingMode.HALF_UP));
        dto.setRemanente(remanente);
        dto.setDetalles(detalles);

        return dto;
    }
}