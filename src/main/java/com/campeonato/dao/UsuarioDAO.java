package com.campeonato.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.campeonato.dao.registro.BaseDAO;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
public class UsuarioDAO extends BaseDAO
{

    private static final Logger log           = LogManager.getLogger(UsuarioDAO.class);
    private String              OPERACION_URL = "SELECT a.url, p.ver, p.consultar, p.guardar, p.editar, p.exportar "
            + "FROM usuarios u " + "JOIN permisos     p ON u.rol_id        = p.rol_id "
            + "JOIN aplicaciones a ON p.aplicacion_id = a.id " + "WHERE u.username = ? " + "AND a.url IS NOT NULL";
    private String              ROL_USUARIO   = "SELECT r.nombre " + "FROM usuarios u "
            + "JOIN roles r ON u.rol_id = r.id " + "WHERE u.username = ?";
    private String              USUARIO       = "SELECT COUNT(*) FROM usuarios WHERE username = ? AND password = ?";

    public UsuarioDAO(Connection conn)
    {
        super(conn);
    }

    public boolean existeUsuario(String username, String password) throws CampeonatoException
    {


        try (PreparedStatement ps = conn.prepareStatement(USUARIO))
        {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    boolean existe = rs.getInt(1) > 0;
                    return existe;
                }
            }
        } catch (SQLException e)
        {
            throw manejarError("existeUsuario", e);
        }

        return false;
    }

    public Map<String, Set<String>> getOperacionesPorUrl(String username) throws CampeonatoException
    {


        Map<String, Set<String>> mapa = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(OPERACION_URL))
        {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    String      url         = rs.getString("url");
                    Set<String> operaciones = new HashSet<>();

                    if ("S".equals(rs.getString("ver")))
                    {
                        operaciones.add("VER");
                    }
                    if ("S".equals(rs.getString("consultar")))
                    {
                        operaciones.add("CONSULTAR");
                    }
                    if ("S".equals(rs.getString("guardar")))
                    {
                        operaciones.add("GUARDAR");
                    }
                    if ("S".equals(rs.getString("editar")))
                    {
                        operaciones.add("EDITAR");
                    }
                    if ("S".equals(rs.getString("exportar")))
                    {
                        operaciones.add("EXPORTAR");
                    }

                    mapa.put(url, operaciones);
                }
            }
        } catch (SQLException e)
        {
            throw manejarError("getOperacionesPorUrl", e);
        }

        return mapa;
    }

    public String getRolUsuario(String username) throws CampeonatoException
    {


        try (PreparedStatement ps = conn.prepareStatement(ROL_USUARIO))
        {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    String rol = rs.getString("nombre");
                    return rol;
                }
            }
        } catch (SQLException e)
        {
            throw manejarError("getRolUsuario", e);
        }

        return null;
    }
}