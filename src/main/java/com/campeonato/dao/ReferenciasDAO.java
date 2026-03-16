package com.campeonato.dao;

import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.modelo.ui.ReferenciaItem;

import jakarta.ejb.Stateless;

import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ReferenciasDAO {

    private static final Logger log = LogManager.getLogger(ReferenciasDAO.class);

    // ============================================================
    // Método genérico — consulta cualquier tabla de referencia
    // que tenga columnas id y nombre
    //
    // Uso:
    //   List<ReferenciaItem> tipos = dao.getListaReferencia(conn, "tipos_documento");
    // ============================================================
    public List<ReferenciaItem> getListaReferencia(Connection conn, String tabla)
            throws CampeonatoException {

        LogManager.inicio(log, "getListaReferencia: " + tabla);

        List<ReferenciaItem> lista = new ArrayList<>();

        String sql = "SELECT id, nombre FROM " + tabla + " ORDER BY nombre";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ReferenciaItem(
                    rs.getString("id"),
                    rs.getString("nombre")
                ));
            }

        } catch (SQLException e) {
            throw new CampeonatoException(CampeonatoException.DAO_CONSULTA, e);
        }

        LogManager.fin(log, "getListaReferencia: " + tabla);
        return lista;
    }

    // ============================================================
    // Métodos específicos por tabla de referencia
    // Agrega uno por cada tabla nueva que necesites
    // ============================================================

    public List<ReferenciaItem> getTiposDocumento(Connection conn)
            throws CampeonatoException {
        return getListaReferencia(conn, "tipos_documento");
    }

    public List<ReferenciaItem> getRoles(Connection conn)
            throws CampeonatoException {
        return getListaReferencia(conn, "roles");
    }

    public List<ReferenciaItem> getTiposEquipo(Connection conn)
            throws CampeonatoException {
        return getListaReferencia(conn, "tipos_equipo");
    }

    public List<ReferenciaItem> getCategorias(Connection conn)
            throws CampeonatoException {
        return getListaReferencia(conn, "categorias");
    }

    public List<ReferenciaItem> getEstadosPartido(Connection conn)
            throws CampeonatoException {
        return getListaReferencia(conn, "estados_partido");
    }

    public List<ReferenciaItem> getPosicionesJugador(Connection conn)
            throws CampeonatoException {
        return getListaReferencia(conn, "posiciones_jugador");
    }

    public List<ReferenciaItem> getPaises(Connection conn)
            throws CampeonatoException {
        return getListaReferencia(conn, "paises");
    }

    // ============================================================
    // Para agregar una nueva referencia:
    // 1. Agrega el método específico aquí
    // 2. Agrega el atributo en ReferenciasCache
    // 3. Agrega el getter en ReferenciasCache
    // ============================================================
}