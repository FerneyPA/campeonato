package com.campeonato.referencias;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;

import com.campeonato.connection.CampeonatoConnection;
import com.campeonato.connection.ConnectionManager;
import com.campeonato.dao.ReferenciasDAO;
import com.campeonato.dao.registro.DAOFactory;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.modelo.ui.ReferenciaItem;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class ReferenciasCache
{

    private static final Logger log = LogManager.getLogger(ReferenciasCache.class);

    @Inject
    private ConnectionManager connectionManager;

    private List<ReferenciaItem> tiposDocumento    = Collections.emptyList();
    private List<ReferenciaItem> roles             = Collections.emptyList();
    private List<ReferenciaItem> tiposEquipo       = Collections.emptyList();
    private List<ReferenciaItem> categorias        = Collections.emptyList();
    private List<ReferenciaItem> estadosPartido    = Collections.emptyList();
    private List<ReferenciaItem> posicionesJugador = Collections.emptyList();
    private List<ReferenciaItem> paises            = Collections.emptyList();

    @PostConstruct
    public void cargar()
    {

        log.info("[ReferenciasCache] Cargando referencias...");

        CampeonatoConnection conn = null;

        try
        {
            conn = connectionManager.getConnectionSeguridad();
            ReferenciasDAO daoSeguridad = DAOFactory.getReferenciasDAO(conn);

            tiposDocumento = daoSeguridad.getTiposDocumento();
            roles          = daoSeguridad.getRoles();

            conn.close();

            conn = connectionManager.getConnectionCampeonato();
            ReferenciasDAO daoCampeonato = DAOFactory.getReferenciasDAO(conn);

            tiposEquipo       = daoCampeonato.getTiposEquipo();
            categorias        = daoCampeonato.getCategorias();
            estadosPartido    = daoCampeonato.getEstadosPartido();
            posicionesJugador = daoCampeonato.getPosicionesJugador();
            paises            = daoCampeonato.getPaises();

            log.info("[ReferenciasCache] Referencias cargadas correctamente.");

        } catch (CampeonatoException e)
        {
            log.error("[ReferenciasCache] Error al cargar referencias: {}", e.toString());

        } catch (Exception e)
        {
            log.error("[ReferenciasCache] Error inesperado al cargar referencias", e);

        } finally
        {
            if (conn != null)
            {
                conn.close();
            }
        }
    }

    public List<ReferenciaItem> getCategorias()
    {
        return Collections.unmodifiableList(categorias);
    }

    public List<ReferenciaItem> getEstadosPartido()
    {
        return Collections.unmodifiableList(estadosPartido);
    }

    public List<ReferenciaItem> getPaises()
    {
        return Collections.unmodifiableList(paises);
    }

    public List<ReferenciaItem> getPosicionesJugador()
    {
        return Collections.unmodifiableList(posicionesJugador);
    }

    public List<ReferenciaItem> getRoles()
    {
        return Collections.unmodifiableList(roles);
    }

    public List<ReferenciaItem> getTiposDocumento()
    {
        return Collections.unmodifiableList(tiposDocumento);
    }

    public List<ReferenciaItem> getTiposEquipo()
    {
        return Collections.unmodifiableList(tiposEquipo);
    }

    public void refrescar()
    {
        log.info("[ReferenciasCache] Refrescando referencias manualmente...");
        cargar();
    }
}