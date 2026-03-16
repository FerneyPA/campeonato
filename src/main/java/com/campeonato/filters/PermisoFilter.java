package com.campeonato.filters;

import com.campeonato.beans.UsuarioSesion;

import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("*.xhtml")
public class PermisoFilter implements Filter {

    @Inject
    private UsuarioSesion usuarioSesion;

    // ============================================================
    // URLs que no requieren autenticación ni permisos
    // ============================================================
    private static final String[] URLS_PUBLICAS = {
        "/login.xhtml",
        "/acceso-denegado.xhtml"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Sin inicialización especial
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getServletPath();

        // 1. URL pública — deja pasar sin verificar
        if (esUrlPublica(url)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. No autenticado — redirige al login
        if (usuarioSesion == null || !usuarioSesion.isAutenticado()) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
            return;
        }

        // 3. Verifica si la URL está en las URLs permitidas de la sesión
        // Las URLs vienen de la tabla aplicaciones cargadas al hacer login
        if (usuarioSesion.tieneAcceso(url)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/acceso-denegado.xhtml");
        }
    }

    @Override
    public void destroy() {
        // Sin limpieza especial
    }

    private boolean esUrlPublica(String url) {
        for (String urlPublica : URLS_PUBLICAS) {
            if (url.equals(urlPublica)) {
                return true;
            }
        }
        return false;
    }
}