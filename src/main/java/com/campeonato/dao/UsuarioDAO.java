package com.campeonato.dao;

import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;

import jakarta.ejb.Stateless;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class UsuarioDAO {

    private static final Logger log = LogManager.getLogger(UsuarioDAO.class);

    // ============================================================
    // Verifica si las credenciales son correctas
    // ============================================================
    public boolean existeUsuario(Connection conn, String username, String password)
            throws CampeonatoException {

        LogManager.inicio(log, "existeUsuario");

        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ? AND password = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean existe = rs.getInt(1) > 0;
                    LogManager.fin(log, "existeUsuario");
                    return existe;
                }
            }
        } catch (SQLException e) {
            throw new CampeonatoException(CampeonatoException.DAO_CONSULTA, e);
        }

        return false;
    }

    // ============================================================
    // Obtiene el nombre del rol del usuario
    // ============================================================
    public String getRolUsuario(Connection conn, String username)
            throws CampeonatoException {

        LogManager.inicio(log, "getRolUsuario");

        String sql = "SELECT r.nombre " +
                     "FROM usuarios u " +
                     "JOIN roles r ON u.rol_id = r.id " +
                     "WHERE u.username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("nombre");
                    LogManager.fin(log, "getRolUsuario");
                    return rol;
                }
            }
        } catch (SQLException e) {
            throw new CampeonatoException(CampeonatoException.DAO_CONSULTA, e);
        }

        return null;
    }

    // ============================================================
    // Obtiene todas las URLs permitidas para el usuario
    // a través de la tabla aplicaciones unida a roles_permisos
    // ============================================================
    public Set<String> getUrlsPermitidas(Connection conn, String username)
            throws CampeonatoException {

        LogManager.inicio(log, "getUrlsPermitidas");

        Set<String> urls = new HashSet<>();

        String sql = "SELECT a.url " +
                     "FROM usuarios u " +
                     "JOIN roles r           ON u.rol_id      = r.id " +
                     "JOIN roles_permisos rp  ON r.id          = rp.rol_id " +
                     "JOIN aplicaciones a     ON rp.id         = a.roles_permisos_id " +
                     "WHERE u.username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    urls.add(rs.getString("url"));
                }
            }
        } catch (SQLException e) {
            throw new CampeonatoException(CampeonatoException.DAO_CONSULTA, e);
        }

        LogManager.fin(log, "getUrlsPermitidas");
        return urls;
    }
}