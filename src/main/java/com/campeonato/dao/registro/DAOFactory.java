package com.campeonato.dao.registro;

import java.sql.Connection;

import com.campeonato.connection.CampeonatoConnection;
import com.campeonato.dao.MenuDAO;
import com.campeonato.dao.ReferenciasDAO;
import com.campeonato.dao.UsuarioDAO;

public class DAOFactory
{

    public static MenuDAO getMenuDAO(CampeonatoConnection conn)
    {
        return new MenuDAO(conn.getConnection());
    }

    public static MenuDAO getMenuDAO(Connection conn)
    {
        return new MenuDAO(conn);
    }

    public static ReferenciasDAO getReferenciasDAO(CampeonatoConnection conn)
    {
        return new ReferenciasDAO(conn.getConnection());
    }

    public static ReferenciasDAO getReferenciasDAO(Connection conn)
    {
        return new ReferenciasDAO(conn);
    }

    public static UsuarioDAO getUsuarioDAO(CampeonatoConnection conn)
    {
        return new UsuarioDAO(conn.getConnection());
    }

    public static UsuarioDAO getUsuarioDAO(Connection conn)
    {
        return new UsuarioDAO(conn);
    }

    private DAOFactory()
    {
    }
}