package mx.dgtic.unam.simin.util;

import mx.dgtic.unam.simin.entity.Instrumento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class InstrumentoUtils {

    public static int calcularDiasPlazo(Integer meses, Integer anos) {
        int dias = 0;
        if (meses != null) dias += meses * 30;
        if (anos != null) dias += anos * 365;
        if (dias == 0) throw new IllegalArgumentException("El instrumento debe tener un plazo definido para calcular días.");
        return dias;
    }

    public static BigDecimal calcularPrecioMercado(BigDecimal tasaBruta, Integer diasPlazo, BigDecimal valorNominal) {
        BigDecimal tasaDecimal = tasaBruta.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal factor = tasaDecimal.multiply(BigDecimal.valueOf(diasPlazo))
                .divide(BigDecimal.valueOf(365), 6, RoundingMode.HALF_UP);
        BigDecimal divisor = BigDecimal.ONE.add(factor);
        return valorNominal.divide(divisor, 4, RoundingMode.HALF_UP);
    }

    public static BigDecimal calcularPrecioMercado(Instrumento instrumento) {
        if (instrumento.getTipoInstrumento() != null) {
            String tipo = instrumento.getTipoInstrumento().getDescripcion().toUpperCase();
            if (tipo.equals("BONO") || tipo.equals("UDIBONOS") || tipo.equals("BONDES")) {
                throw new UnsupportedOperationException("El precio de mercado para BONO, UDIBONOS o BONDES no se calcula automáticamente.");
            }
        }

        if (instrumento.getTasaInteres() == null ||
                instrumento.getValorNominal() == null ||
                (instrumento.getPlazoMeses() == null && instrumento.getPlazoAnos() == null)) {
            throw new IllegalArgumentException("El instrumento debe tener tasa, plazo y valor nominal para calcular el precio de mercado.");
        }

        int diasPlazo = calcularDiasPlazo(instrumento.getPlazoMeses(), instrumento.getPlazoAnos());

        return calcularPrecioMercado(
                instrumento.getTasaInteres(),
                diasPlazo,
                instrumento.getValorNominal()
        );
    }

    public static BigDecimal calcularPrecioMercadoBono(Instrumento instrumento) {
        if (instrumento.getFechaEmision() == null ||
                instrumento.getFechaVencimiento() == null ||
                instrumento.getTasaCupon() == null ||
                instrumento.getFrecuenciaPagos() == null ||
                instrumento.getValorNominal() == null) {
            throw new IllegalArgumentException("Faltan datos obligatorios para calcular el precio de mercado del bono.");
        }

        BigDecimal tasaAnual = instrumento.getTasaCupon().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal valorNominal = instrumento.getValorNominal();

        long dias = Duration.between(instrumento.getFechaEmision(), instrumento.getFechaVencimiento()).toDays();
        if (dias <= 0) throw new IllegalArgumentException("La fecha de vencimiento debe ser posterior a la de emisión.");

        int frecuencia = switch (instrumento.getFrecuenciaPagos()) {
            case Anual -> 1;
            case Semestral -> 2;
            case Trimestral -> 4;
            case Mensual -> 12;
            default -> throw new IllegalArgumentException("Frecuencia no soportada para bonos.");
        };

        int numPeriodos = (int) (dias / (365.0 / frecuencia));
        BigDecimal tasaPorPeriodo = tasaAnual.divide(BigDecimal.valueOf(frecuencia), 6, RoundingMode.HALF_UP);

        BigDecimal valorPresenteCupones = BigDecimal.ZERO;
        for (int i = 1; i <= numPeriodos; i++) {
            BigDecimal cupon = valorNominal.multiply(tasaPorPeriodo);
            BigDecimal descuento = BigDecimal.ONE.add(tasaPorPeriodo).pow(i);
            valorPresenteCupones = valorPresenteCupones.add(cupon.divide(descuento, 6, RoundingMode.HALF_UP));
        }

        BigDecimal valorPresenteFinal = valorNominal.divide(
                BigDecimal.ONE.add(tasaPorPeriodo).pow(numPeriodos),
                6, RoundingMode.HALF_UP
        );

        return valorPresenteCupones.add(valorPresenteFinal).setScale(4, RoundingMode.HALF_UP);
    }
}