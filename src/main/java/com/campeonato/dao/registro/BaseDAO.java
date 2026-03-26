package com.campeonato.dao.registro;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;

import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;

public abstract class BaseDAO
{

    private static final Logger log = LogManager.getLogger(BaseDAO.class);

    protected final Connection conn;

    public BaseDAO(Connection conn)
    {
        this.conn = conn;
    }

    protected CampeonatoException manejarError(String metodo, SQLException e)
    {
        log.error("[{}] Error en {}: {}", getClass().getSimpleName(), metodo, e.getMessage(), e);
        return new CampeonatoException(CampeonatoException.DAO_CONSULTA, e);
    }

    protected void validarConexion() throws CampeonatoException
    {
        try
        {
            if (conn == null || conn.isClosed())
            {
                throw new CampeonatoException(CampeonatoException.DAO_CONEXION);
            }
        } catch (SQLException e)
        {
            throw new CampeonatoException(CampeonatoException.DAO_CONEXION, e);
        }
    }
}