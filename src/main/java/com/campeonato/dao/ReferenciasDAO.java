package com.campeonato.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.campeonato.dao.registro.BaseDAO;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.modelo.ui.ReferenciaItem;

public class ReferenciasDAO extends BaseDAO
{

    private static final Logger log = LogManager.getLogger(ReferenciasDAO.class);

    public ReferenciasDAO(Connection conn)
    {
        super(conn);
    }

    public List<ReferenciaItem> getCategorias() throws CampeonatoException
    {
        return getListaReferencia("categorias");
    }

    public List<ReferenciaItem> getEstadosPartido() throws CampeonatoException
    {
        return getListaReferencia("estados_partido");
    }

    public List<ReferenciaItem> getListaReferencia(String tabla) throws CampeonatoException
    {

        LogManager.inicio(log, "getListaReferencia: " + tabla);

        List<ReferenciaItem> lista = new ArrayList<>();

        String sql = "SELECT id, nombre FROM " + tabla + " ORDER BY nombre";

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery())
        {

            while (rs.next())
            {
                lista.add(new ReferenciaItem(rs.getString("id"), rs.getString("nombre")));
            }

        } catch (SQLException e)
        {
            throw manejarError("getListaReferencia: " + tabla, e);
        }

        LogManager.fin(log, "getListaReferencia: " + tabla);
        return lista;
    }

    public List<ReferenciaItem> getPaises() throws CampeonatoException
    {
        return getListaReferencia("paises");
    }

    public List<ReferenciaItem> getPosicionesJugador() throws CampeonatoException
    {
        return getListaReferencia("posiciones_jugador");
    }

    public List<ReferenciaItem> getRoles() throws CampeonatoException
    {
        return getListaReferencia("roles");
    }

    public List<ReferenciaItem> getTiposDocumento() throws CampeonatoException
    {
        return getListaReferencia("tipos_documento");
    }

    public List<ReferenciaItem> getTiposEquipo() throws CampeonatoException
    {
        return getListaReferencia("tipos_equipo");
    }
}