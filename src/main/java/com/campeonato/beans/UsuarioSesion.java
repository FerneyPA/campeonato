package com.campeonato.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

@Named
@SessionScoped
public class UsuarioSesion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String      username;
    private String      rol;
    private boolean     autenticado;
    private Set<String> urlsPermitidas = Collections.emptySet();

    // ============================================================
    // Se llama desde LoginBusiness al autenticar exitosamente
    // ============================================================
    public void iniciarSesion(String username, String rol, Set<String> urlsPermitidas) {
        this.username       = username;
        this.rol            = rol;
        this.urlsPermitidas = urlsPermitidas != null ? urlsPermitidas : Collections.emptySet();
        this.autenticado    = true;
    }

    public void cerrarSesion() {
        this.username       = null;
        this.rol            = null;
        this.urlsPermitidas = Collections.emptySet();
        this.autenticado    = false;
    }

    // ============================================================
    // Verificaciones de acceso
    // ============================================================

    public boolean isAutenticado() {
        return autenticado;
    }

    public boolean tieneAcceso(String url) {
        return urlsPermitidas.contains(url);
    }

    public boolean tieneRol(String nombreRol) {
        return nombreRol != null && nombreRol.equalsIgnoreCase(rol);
    }

    // ============================================================
    // Getters
    // ============================================================

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }

    public Set<String> getUrlsPermitidas() {
        return Collections.unmodifiableSet(urlsPermitidas);
    }
}