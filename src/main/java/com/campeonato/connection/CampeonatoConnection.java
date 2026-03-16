package com.campeonato.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class CampeonatoConnection {

    private final Connection conn;
    private boolean errorOcurrido = false;

    // ============================================================
    // Constructor — recibe la conexión abierta por ConnectionManager
    // Por defecto viene con READ_COMMITTED (lecturasSucias = false)
    // ============================================================

    public CampeonatoConnection(Connection conn) {
        this.conn = conn;
    }

    // ============================================================
    // Cambia el nivel de aislamiento después de abrir la conexión
    //   true  → READ_UNCOMMITTED (lecturas sucias permitidas)
    //   false → READ_COMMITTED   (solo datos confirmados)
    // ============================================================

    public CampeonatoConnection lecturasSucias(boolean activar) throws SQLException {
        conn.setTransactionIsolation(
            activar
                ? Connection.TRANSACTION_READ_UNCOMMITTED
                : Connection.TRANSACTION_READ_COMMITTED
        );
        return this;
    }

    // ============================================================
    // Expone la Connection interna para usarla en los DAOs
    // ============================================================

    public Connection get() {
        return conn;
    }

    // ============================================================
    // Marca la conexión como fallida para que close haga rollback
    // El Business llama este método en el catch
    // ============================================================

    public void error() {
        this.errorOcurrido = true;
    }

    // ============================================================
    // close — hace commit si no hubo error, rollback si hubo error
    // El Business solo necesita llamar close() en el finally
    // ============================================================

    public void close() {
        try {
            if (errorOcurrido) {
                conn.rollback();
            } else {
                conn.commit();
            }
        } catch (SQLException e) {
            System.err.println("[CampeonatoConnection] Error en " + (errorOcurrido ? "rollback" : "commit") + ": " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                System.err.println("[CampeonatoConnection] Error al cerrar: " + e.getMessage());
            }
        }
    }
}