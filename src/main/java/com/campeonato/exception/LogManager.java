package com.campeonato.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {

    // ============================================================
    // Obtiene un logger para la clase que lo solicita
    // Uso: private static final Logger log = LogManager.getLogger(MiClase.class);
    // ============================================================

    public static Logger getLogger(Class<?> clase) {
        return LoggerFactory.getLogger(clase);
    }

    // ============================================================
    // Métodos de conveniencia — registran CampeonatoException
    // con su código y mensaje en el nivel adecuado
    // ============================================================

    public static void info(Logger log, String mensaje) {
        log.info(mensaje);
    }

    public static void warn(Logger log, String mensaje) {
        log.warn(mensaje);
    }

    public static void warn(Logger log, String mensaje, CampeonatoException e) {
        log.warn("{} - {}", mensaje, e.toString());
    }

    public static void error(Logger log, String mensaje, CampeonatoException e) {
        log.error("{} - {} | Causa: {}", mensaje, e.toString(),
            e.getCause() != null ? e.getCause().getMessage() : "sin causa", e);
    }

    public static void error(Logger log, String mensaje, Exception e) {
        log.error("{} | Causa: {}", mensaje, e.getMessage(), e);
    }

    // ============================================================
    // Registra el inicio y fin de un proceso — útil para medir
    // tiempos de ejecución en producción
    // ============================================================

    public static void inicio(Logger log, String proceso) {
        log.info("[INICIO] {}", proceso);
    }

    public static void fin(Logger log, String proceso) {
        log.info("[FIN] {}", proceso);
    }
}