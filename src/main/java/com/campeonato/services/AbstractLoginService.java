package com.campeonato.services;

import com.campeonato.exception.LogManager;
import org.slf4j.Logger;

public abstract class AbstractLoginService implements ILoginService {

    private static final Logger log = LogManager.getLogger(AbstractLoginService.class);

    // ============================================================
    // Métodos de logging reutilizables para todas las
    // implementaciones del servicio de login
    // ============================================================

    protected void logInicioLogin(String username) {
        log.info("[SERVICE] Inicio de login para usuario: {}", username);
    }

    protected void logFinLogin(String username, boolean exitoso) {
        if (exitoso) {
            log.info("[SERVICE] Login exitoso para usuario: {}", username);
        } else {
            log.warn("[SERVICE] Login fallido para usuario: {}", username);
        }
    }

    protected void logInicioLogout(String username) {
        log.info("[SERVICE] Inicio de logout para usuario: {}", username);
    }

    protected void logFinLogout(String username) {
        log.info("[SERVICE] Logout completado para usuario: {}", username);
    }

    protected void logError(String username, Exception e) {
        log.error("[SERVICE] Error en proceso de login para usuario: {} — {}", username, e.getMessage(), e);
    }
}