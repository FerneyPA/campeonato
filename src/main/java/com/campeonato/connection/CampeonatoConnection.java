package com.campeonato.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class CampeonatoConnection
{

    private final Connection conn;
    private boolean          errorOcurrido = false;

    public CampeonatoConnection(Connection conn)
    {
        this.conn = conn;
    }

    public void close()
    {
        try
        {
            if (!conn.getAutoCommit())
            { // ← solo si autoCommit es false
                if (errorOcurrido)
                {
                    conn.rollback();
                } else
                {
                    conn.commit();
                }
            }
        } catch (SQLException e)
        {
            System.err.println("[CampeonatoConnection] Error en " + (errorOcurrido ? "rollback" : "commit") + ": "
                    + e.getMessage());
        } finally
        {
            try
            {
                conn.close();
            } catch (SQLException e)
            {
                System.err.println("[CampeonatoConnection] Error al cerrar: " + e.getMessage());
            }
        }
    }

    public void error()
    {
        errorOcurrido = true;
    }

    public Connection getConnection()
    {
        return conn;
    }

    public CampeonatoConnection lecturasSucias(boolean activar) throws SQLException
    {
        conn.setTransactionIsolation(
                activar ? Connection.TRANSACTION_READ_UNCOMMITTED : Connection.TRANSACTION_READ_COMMITTED);
        return this;
    }
}