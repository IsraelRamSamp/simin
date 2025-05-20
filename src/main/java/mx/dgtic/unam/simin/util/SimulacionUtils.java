package mx.dgtic.unam.simin.util;

public class SimulacionUtils {

    private SimulacionUtils() {
        // Evitar instanciación
    }

    public static String formatearPlazoTexto(long dias) {
        if (dias >= 365) {
            int anios = (int) dias / 365;
            return anios + " año" + (anios > 1 ? "s" : "");
        } else if (dias >= 30) {
            int meses = (int) dias / 30;
            return meses + " mes" + (meses > 1 ? "es" : "");
        } else {
            return dias + " día" + (dias > 1 ? "s" : "");
        }
    }
}