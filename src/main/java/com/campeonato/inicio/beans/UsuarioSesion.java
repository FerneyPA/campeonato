package com.campeonato.inicio.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class UsuarioSesion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String rol;
    private boolean autenticado;

    // Mapa de permisos por URL
    private Map<String, Set<String>> operacionesPorUrl = Collections.emptyMap();

    // ============================================================
    // MÉTODOS DE SESIÓN
    // ============================================================

    public void iniciarSesion(String username, String rol, Map<String, Set<String>> operacionesPorUrl) {
        this.username = username;
        this.rol = rol;
        this.operacionesPorUrl = operacionesPorUrl != null ? operacionesPorUrl : Collections.emptyMap();
        this.autenticado = true;
    }

    public void cerrarSesion() {
        this.username = null;
        this.rol = null;
        this.operacionesPorUrl = Collections.emptyMap();
        this.autenticado = false;
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public Map<String, Set<String>> getOperacionesPorUrl() {
        return Collections.unmodifiableMap(operacionesPorUrl);
    }

    // ============================================================
    // CONTROL DE PERMISOS
    // ============================================================

    /**
     * Verifica si el usuario tiene acceso a una URL.
     * Se considera acceso válido si existe al menos una operación asociada.
     */
    public boolean tieneAcceso(String url) {
        if (url == null) return false;
        Set<String> ops = operacionesPorUrl.get(url);
        return ops != null && !ops.isEmpty();
    }

    /**
     * Verifica si el usuario tiene una operación específica sobre una URL.
     */
    public boolean tieneOperacion(String url, String operacion) {
        if (url == null || operacion == null) return false;
        Set<String> ops = operacionesPorUrl.get(url);
        return ops != null && ops.contains(operacion.toUpperCase());
    }

    /**
     * Verifica si el usuario tiene un rol específico.
     */
    public boolean tieneRol(String nombreRol) {
        return nombreRol != null && nombreRol.equalsIgnoreCase(rol);
    }
}