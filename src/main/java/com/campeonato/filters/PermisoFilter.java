package com.campeonato.filters;

import java.io.IOException;

import com.campeonato.inicio.beans.UsuarioSesion;

import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PermisoFilter implements Filter {

    private static final String[] URLS_PUBLICAS = {
        "/login.xhtml",
        "/acceso-denegado.xhtml",
    };

    @Inject
    private UsuarioSesion usuarioSesion;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String url = httpRequest.getServletPath();

        System.out.println("URL FILTRO: " + url);

        // ============================================================
        // 1. Recursos JSF/PrimeFaces — SIEMPRE permitir
        // ============================================================
        if (uri.contains("/jakarta.faces.resource/")
                || uri.contains("/javax.faces.resource/")
                || uri.contains("/resources/")
                || uri.contains("/primefaces/")) {

            chain.doFilter(request, response);
            return;
        }
        
     // 2. Home accesible para cualquier usuario autenticado
        if (url.equals("/home.xhtml")) {
            if (usuarioSesion != null && usuarioSesion.isAutenticado()) {
                chain.doFilter(request, response);
                return;
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
                return;
            }
        }


        // ============================================================
        // 2. URL pública
        // ============================================================
        if (esUrlPublica(url)) {
            chain.doFilter(request, response);
            return;
        }

        // ============================================================
        // 3. Usuario no autenticado
        // ============================================================
        if (usuarioSesion == null || !usuarioSesion.isAutenticado()) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
            return;
        }

        // ============================================================
        // 4. Verificación de permisos
        // ============================================================
        String urlNormalizada = normalizar(url, httpRequest);

        if (usuarioSesion.tieneAcceso(urlNormalizada)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/acceso-denegado.xhtml");
        }
    }

    private String normalizar(String url, HttpServletRequest httpRequest) {
        if (url == null) return "";
        return url.replaceFirst("^/faces", "")
                  .replaceFirst("^" + httpRequest.getContextPath(), "")
                  .replaceAll("\\?.*$", "");
    }

    @Override
    public void destroy() {}

    private boolean esUrlPublica(String url) {
        for (String urlPublica : URLS_PUBLICAS) {
            if (url.equals(urlPublica)) {
                return true;
            }
        }
        return false;
    }
}