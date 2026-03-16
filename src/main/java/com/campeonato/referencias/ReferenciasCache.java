package com.campeonato.referencias;

import com.campeonato.connection.CampeonatoConnection;
import com.campeonato.connection.ConnectionManager;
import com.campeonato.dao.ReferenciasDAO;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.modelo.ui.ReferenciaItem;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;

@Named
@ApplicationScoped
public class ReferenciasCache {

    private static final Logger log = LogManager.getLogger(ReferenciasCache.class);

    @Inject
    private ConnectionManager connectionManager;

    @Inject
    private ReferenciasDAO referenciasDAO;

    // ============================================================
    // Listas de referencia — una por cada tabla
    // ============================================================
    private List<ReferenciaItem> tiposDocumento  = Collections.emptyList();
    private List<ReferenciaItem> roles            = Collections.emptyList();
    private List<ReferenciaItem> tiposEquipo      = Collections.emptyList();
    private List<ReferenciaItem> categorias       = Collections.emptyList();
    private List<ReferenciaItem> estadosPartido   = Collections.emptyList();
    private List<ReferenciaItem> posicionesJugador = Collections.emptyList();
    private List<ReferenciaItem> paises           = Collections.emptyList();

    // ============================================================
    // Se ejecuta automáticamente al arrancar el servidor
    // Carga todas las referencias de una sola vez
    // ============================================================
    @PostConstruct
    public synchronized void cargar() {

        log.info("[ReferenciasCache] Cargando referencias...");

        CampeonatoConnection conn = null;

        try {
            conn = connectionManager.getConnectionSeguridad();

            tiposDocumento   = referenciasDAO.getTiposDocumento(conn.get());
            roles             = referenciasDAO.getRoles(conn.get());

            conn.close();

            // Referencias del módulo campeonato
            conn = connectionManager.getConnectionCampeonato();

            tiposEquipo       = referenciasDAO.getTiposEquipo(conn.get());
            categorias        = referenciasDAO.getCategorias(conn.get());
            estadosPartido    = referenciasDAO.getEstadosPartido(conn.get());
            posicionesJugador = referenciasDAO.getPosicionesJugador(conn.get());
            paises            = referenciasDAO.getPaises(conn.get());

            log.info("[ReferenciasCache] Referencias cargadas correctamente.");

        } catch (CampeonatoException e) {
            log.error("[ReferenciasCache] Error al cargar referencias: {}", e.toString());

        } catch (Exception e) {
            log.error("[ReferenciasCache] Error inesperado al cargar referencias", e);

        } finally {
            if (conn != null) conn.close();
        }
    }

    // ============================================================
    // Refresca todas las referencias sin reiniciar el servidor
    // Se puede llamar desde un bean de administración
    //
    // Uso desde un bean:
    //   referenciasCache.refrescar();
    //   mensajeInfo("Referencias actualizadas correctamente.");
    // ============================================================
    public void refrescar() {
        log.info("[ReferenciasCache] Refrescando referencias manualmente...");
        cargar();
    }

    // ============================================================
    // Getters — disponibles en todos los XHTML via #{referenciasCache}
    // ============================================================

    public List<ReferenciaItem> getTiposDocumento() {
        return Collections.unmodifiableList(tiposDocumento);
    }

    public List<ReferenciaItem> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public List<ReferenciaItem> getTiposEquipo() {
        return Collections.unmodifiableList(tiposEquipo);
    }

    public List<ReferenciaItem> getCategorias() {
        return Collections.unmodifiableList(categorias);
    }

    public List<ReferenciaItem> getEstadosPartido() {
        return Collections.unmodifiableList(estadosPartido);
    }

    public List<ReferenciaItem> getPosicionesJugador() {
        return Collections.unmodifiableList(posicionesJugador);
    }

    public List<ReferenciaItem> getPaises() {
        return Collections.unmodifiableList(paises);
    }
}