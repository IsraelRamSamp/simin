package mx.dgtic.unam.simin.simulation;

import lombok.extern.slf4j.Slf4j;
import mx.dgtic.unam.simin.entity.Instrumento;
import mx.dgtic.unam.simin.simulation.strategy.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InstrumentoCalculadorFactory {

    private static final Map<String, InstrumentoCalculador> calculadores = new HashMap<>();

    static {
        calculadores.put("BONO", new BonoCalculador());
        calculadores.put("CETE", new CeteCalculador());
        calculadores.put("BONDES", new BondesCalculador());
        calculadores.put("UDIBONOS", new UdibonosCalculador());
        calculadores.put("BONDDIA", new BonddiaCalculador());
        calculadores.put("REPORTO", new ReportoCalculador());
    }

    public static InstrumentoCalculador obtenerCalculador(Instrumento instrumento) {
        String tipo = instrumento.getTipoInstrumento().getDescripcion().toUpperCase();
        log.info("Solicitando calculador para tipo de instrumento: {}", tipo);

        InstrumentoCalculador calculador = calculadores.get(tipo);
        if (calculador == null) {
            log.error("No se encontró calculador para el tipo de instrumento: {}", tipo);
            throw new IllegalArgumentException("No hay un calculador disponible para el tipo de instrumento: " + tipo);
        }

        log.info("Calculador encontrado para tipo: {}", tipo);
        return calculador;
    }
}