package mx.dgtic.unam.simin.simulation.strategy;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.util.SimulacionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BondesCalculador implements InstrumentoCalculador {

    private static final BigDecimal TASA_BONDDIA = new BigDecimal("0.0893"); // 8.93%
    private static final BigDecimal PRECIO_BONDDIA = new BigDecimal("2.17"); // Precio por título BONDDIA
    private static final BigDecimal TASA_ISR = new BigDecimal("0.057241");

    @Override
    public SimulacionDto calcular(Instrumento instrumento, BigDecimal monto, long dias) {
        SimulacionDto dto = new SimulacionDto();
        List<DetalleSimulacionDto> detalles = new ArrayList<>();

        BigDecimal precioMercado = instrumento.getPrecioMercado();
        BigDecimal valorNominal = instrumento.getValorNominal();

        BigDecimal tasaCupon = instrumento.getTasaCupon();
        if (tasaCupon == null) {
            throw new IllegalArgumentException("La tasa cupón del instrumento BONDES no puede ser nula.");
        }
        BigDecimal tasaReferencia = tasaCupon.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        // Cálculo de títulos principales
        int titulos = monto.divide(precioMercado, 0, RoundingMode.DOWN).intValue();
        BigDecimal inversionPrincipal = precioMercado.multiply(BigDecimal.valueOf(titulos)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal remanente = monto.subtract(inversionPrincipal).setScale(2, RoundingMode.HALF_UP);

        // Interés sobre títulos BONDES
        BigDecimal interesPrincipal = BigDecimal.valueOf(titulos)
                .multiply(valorNominal)
                .multiply(tasaReferencia)
                .multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        // Inversión del remanente en BONDDIA
        int titulosBonddia = remanente.divide(PRECIO_BONDDIA, 0, RoundingMode.DOWN).intValue();
        BigDecimal inversionBonddia = PRECIO_BONDDIA.multiply(BigDecimal.valueOf(titulosBonddia)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interesBonddia = inversionBonddia
                .multiply(TASA_BONDDIA)
                .multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal remanenteFinal = remanente.subtract(inversionBonddia).setScale(2, RoundingMode.HALF_UP);

        // ISR solo sobre el interés gravable (BONDES)
        BigDecimal isr = interesPrincipal.multiply(TASA_ISR).setScale(2, RoundingMode.HALF_UP);

        BigDecimal valorFinal = monto.add(interesPrincipal).add(interesBonddia).subtract(isr).setScale(2, RoundingMode.HALF_UP);

        // Llenado del detalle
        DetalleSimulacionDto detalle = new DetalleSimulacionDto();
        detalle.setNombreInstrumento(instrumento.getNombre());
        detalle.setTipoInstrumento("BONDES");
        detalle.setTitulos(titulos);
        detalle.setTasaBruta(tasaCupon.setScale(2, RoundingMode.HALF_UP));
        detalle.setInversion(inversionPrincipal);
        detalle.setInteres(interesPrincipal);
        detalle.setTitulosBonddia(titulosBonddia);
        detalle.setInversionBonddia(inversionBonddia);
        detalle.setInteresBonddia(interesBonddia);
        detalle.setRemanente(remanenteFinal);
        detalle.setValorFinal(valorFinal);
        detalle.setMontoInvertido(monto);
        detalle.setIsrCalculado(isr);
        detalle.setPlazoTexto(SimulacionUtils.formatearPlazoTexto(dias));

        detalles.add(detalle);

        dto.setDetalles(detalles);
        dto.setInteresBruto(interesPrincipal.add(interesBonddia));
        dto.setIsr(isr);
        dto.setValorFinal(valorFinal);
        dto.setRendimiento(valorFinal.subtract(monto).setScale(2, RoundingMode.HALF_UP));
        dto.setRemanente(remanenteFinal);

        return dto;
    }
}