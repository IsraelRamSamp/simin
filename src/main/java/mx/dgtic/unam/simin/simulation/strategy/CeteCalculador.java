package mx.dgtic.unam.simin.simulation.strategy;

import mx.dgtic.unam.simin.dto.DetalleSimulacionDto;
import mx.dgtic.unam.simin.dto.SimulacionDto;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.util.SimulacionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CeteCalculador implements InstrumentoCalculador {

    private static final BigDecimal TASA_ISR = new BigDecimal("0.057241");
    private static final BigDecimal PRECIO_BONDDIA = new BigDecimal("2.20");
    private static final BigDecimal TASA_BONDDIA = new BigDecimal("8.93").divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

    @Override
    public SimulacionDto calcular(Instrumento instrumento, BigDecimal monto, long dias) {
        SimulacionDto dto = new SimulacionDto();
        List<DetalleSimulacionDto> detalles = new ArrayList<>();

        // === Datos base del CETE ===
        BigDecimal tasaCete = instrumento.getTasaInteres().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal valorNominal = instrumento.getValorNominal();

        // Precio de mercado implícito: Valor Nominal / (1 + tasa * (días/365))
        BigDecimal divisor = BigDecimal.ONE.add(tasaCete.multiply(BigDecimal.valueOf(dias)).divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_UP));
        BigDecimal precioMercado = valorNominal.divide(divisor, 4, RoundingMode.HALF_UP);

        // === Compra principal ===
        BigDecimal titulos = monto.divide(precioMercado, 0, RoundingMode.DOWN);
        BigDecimal inversionCete = titulos.multiply(precioMercado).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interesCete = titulos.multiply(valorNominal)
                .multiply(tasaCete)
                .multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        // === Sobrante para BONDDIA ===
        BigDecimal sobrante = monto.subtract(inversionCete).setScale(2, RoundingMode.HALF_UP);
        BigDecimal titulosBonddia = sobrante.divide(PRECIO_BONDDIA, 0, RoundingMode.DOWN);
        BigDecimal inversionBonddia = titulosBonddia.multiply(PRECIO_BONDDIA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interesBonddia = inversionBonddia.multiply(TASA_BONDDIA)
                .multiply(BigDecimal.valueOf(dias))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal remanente = sobrante.subtract(inversionBonddia).setScale(2, RoundingMode.HALF_UP);

        // === Totales ===
        BigDecimal interesBruto = interesCete.add(interesBonddia);
        BigDecimal isr = interesBruto.multiply(TASA_ISR).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorFinal = monto.add(interesBruto).subtract(isr).setScale(2, RoundingMode.HALF_UP);

        // === DTO del CETE ===
        DetalleSimulacionDto detalleCete = new DetalleSimulacionDto();
        detalleCete.setNombreInstrumento(instrumento.getNombre());
        detalleCete.setTipoInstrumento("CETE");
        detalleCete.setTitulos(titulos.intValue());
        detalleCete.setTasaBruta(instrumento.getTasaInteres());
        detalleCete.setInversion(inversionCete);
        detalleCete.setInteres(interesCete);
        detalleCete.setInversionBonddia(inversionBonddia);
        detalleCete.setInteresBonddia(interesBonddia);
        detalleCete.setTitulosBonddia(titulosBonddia.intValue());
        detalleCete.setRemanente(remanente);
        detalleCete.setValorFinal(valorFinal);
        detalleCete.setMontoInvertido(monto);
        detalleCete.setIsrCalculado(isr);
        detalleCete.setPlazoTexto(SimulacionUtils.formatearPlazoTexto(dias));

        detalles.add(detalleCete);

        // === Llenar resultado global ===
        dto.setInteresBruto(interesBruto);
        dto.setIsr(isr);
        dto.setValorFinal(valorFinal);
        dto.setRendimiento(valorFinal.subtract(monto));
        dto.setRemanente(remanente);
        dto.setDetalles(detalles);

        return dto;
    }
}