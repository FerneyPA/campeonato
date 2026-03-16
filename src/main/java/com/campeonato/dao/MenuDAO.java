package com.campeonato.dao;

import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.modelo.MenuItemDto;

import jakarta.ejb.Stateless;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class MenuDAO {

    private static final Logger log = LogManager.getLogger(MenuDAO.class);

    // ============================================================
    // Consulta todos los nodos de menú permitidos para el usuario
    // Devuelve lista plana — el armado del árbol es responsabilidad
    // de MenuBusiness
    // ============================================================
    public List<MenuItemDto> getNodosMenu(Connection conn, String username)
            throws CampeonatoException {

        LogManager.inicio(log, "getNodosMenu");

        List<MenuItemDto> nodos = new ArrayList<>();

        String sql = "SELECT DISTINCT a.id, a.nombre, a.url, a.padre_id, a.orden " +
                     "FROM aplicaciones a " +
                     "LEFT JOIN roles_permisos rp ON a.roles_permisos_id = rp.id " +
                     "LEFT JOIN roles r            ON rp.rol_id           = r.id " +
                     "LEFT JOIN usuarios u         ON u.rol_id            = r.id " +
                     "WHERE u.username = ? OR a.url IS NULL " +
                     "ORDER BY a.padre_id NULLS FIRST, a.orden";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    nodos.add(new MenuItemDto(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("url"),
                        rs.getString("padre_id"),
                        rs.getInt("orden")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new CampeonatoException(CampeonatoException.DAO_CONSULTA, e);
        }

        LogManager.fin(log, "getNodosMenu");
        return nodos;
    }
}