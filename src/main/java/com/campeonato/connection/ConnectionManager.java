package com.campeonato.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConnectionManager
{

    private Connection abrirConexion(DataSource ds) throws SQLException
    {
        if (ds == null)
        {
            throw new SQLException("[ConnectionManager] DataSource no disponible.");
        }
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return conn;
    }

    private Connection abrirConexionDriver(String url, String usuario, String clave) throws SQLException
    {
        Connection conn = DriverManager.getConnection(url, usuario, clave);
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return conn;
    }

    public CampeonatoConnection getConnectionCampeonato() throws SQLException
    {
        return new CampeonatoConnection(abrirConexion(resolverDataSource(PropiedadesDB.getJndiCampeonato())));
    }

    public CampeonatoConnection getConnectionEstadisticas() throws SQLException
    {
        return new CampeonatoConnection(abrirConexion(resolverDataSource(PropiedadesDB.getJndiEstadisticas())));
    }

    public CampeonatoConnection getConnectionSeguridad() throws SQLException
    {
        return new CampeonatoConnection(abrirConexion(resolverDataSource(PropiedadesDB.getJndiSeguridad())));
    }

    public CampeonatoConnection getConnectionTestCampeonato() throws SQLException
    {
        return new CampeonatoConnection(abrirConexionDriver(PropiedadesDB.getDriverUrlCampeonato(),
                PropiedadesDB.getDriverUsuarioCampeonato(), PropiedadesDB.getDriverClaveCampeonato()));
    }

    public CampeonatoConnection getConnectionTestEstadisticas() throws SQLException
    {
        return new CampeonatoConnection(abrirConexionDriver(PropiedadesDB.getDriverUrlEstadisticas(),
                PropiedadesDB.getDriverUsuarioEstadisticas(), PropiedadesDB.getDriverClaveEstadisticas()));
    }

    public CampeonatoConnection getConnectionTestSeguridad() throws SQLException
    {
        return new CampeonatoConnection(abrirConexionDriver(PropiedadesDB.getDriverUrlSeguridad(),
                PropiedadesDB.getDriverUsuarioSeguridad(), PropiedadesDB.getDriverClaveSeguridad()));
    }

    private DataSource resolverDataSource(String jndi) throws SQLException
    {
        try
        {
            InitialContext ctx = new InitialContext();
            return (DataSource) ctx.lookup(jndi);
        } catch (NamingException e)
        {
            throw new SQLException("[ConnectionManager] No se encontró DataSource para JNDI: " + jndi, e);
        }
    }
}