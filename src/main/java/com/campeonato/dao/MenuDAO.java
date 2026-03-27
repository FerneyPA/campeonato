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
import com.campeonato.modelo.MenuItemDto;

public class MenuDAO extends BaseDAO
{

    private static final Logger log  = LogManager.getLogger(MenuDAO.class);
    private String              MENU = "SELECT DISTINCT a.id, a.nombre, a.url, a.padre_id, a.orden "
            + "FROM aplicaciones a  WHERE a.url IS NULL  UNION "
            + "SELECT DISTINCT a.id, a.nombre, a.url, a.padre_id, a.orden  FROM aplicaciones a "
            + "JOIN permisos  p ON p.aplicacion_id = a.id  JOIN usuarios  u ON u.rol_id        = p.rol_id "
            + "WHERE u.username = ?  AND a.url IS NOT NULL ORDER BY ISNULL(padre_id) DESC, padre_id, orden";

    public MenuDAO(Connection conn)
    {
        super(conn);
    }

    public List<MenuItemDto> getNodosMenu(String username) throws CampeonatoException
    {


        List<MenuItemDto> nodos = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(MENU))
        {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    nodos.add(new MenuItemDto(rs.getString("id"), rs.getString("nombre"), rs.getString("url"),
                            rs.getString("padre_id"), rs.getInt("orden")));
                }
            }
        } catch (SQLException e)
        {
            throw manejarError("getNodosMenu", e);
        }

        return nodos;
    }
}