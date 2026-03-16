package com.campeonato.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropiedadesDB {

    // ============================================================
    // Un objeto Properties por cada archivo de módulo
    // Se cargan una sola vez al inicializar la clase
    // ============================================================

    private static final Properties seguridad    = cargar("seguridad.properties");
    private static final Properties campeonato   = cargar("campeonato.properties");
    private static final Properties estadisticas = cargar("estadisticas.properties");

    // ============================================================
    // Método interno que carga un archivo del classpath
    // ============================================================

    private static Properties cargar(String archivo) {
        Properties props = new Properties();
        try (InputStream is = PropiedadesDB.class
                .getClassLoader()
                .getResourceAsStream(archivo)) {

            if (is == null) {
                throw new RuntimeException("[PropiedadesDB] Archivo no encontrado: " + archivo);
            }

            props.load(is);
            System.out.println("[PropiedadesDB] Cargado: " + archivo);

        } catch (IOException e) {
            throw new RuntimeException("[PropiedadesDB] Error al cargar: " + archivo + " — " + e.getMessage(), e);
        }
        return props;
    }

    // ============================================================
    // JNDI — producción
    // ============================================================

    public static String getJndiSeguridad() {
        return seguridad.getProperty("jndi.seguridad");
    }

    public static String getJndiCampeonato() {
        return campeonato.getProperty("jndi.campeonato");
    }

    public static String getJndiEstadisticas() {
        return estadisticas.getProperty("jndi.estadisticas");
    }

    // ============================================================
    // DriverManager — tests y diagnóstico
    // ============================================================

    public static String getDriverUrlSeguridad() {
        return seguridad.getProperty("driver.seguridad.url");
    }

    public static String getDriverUsuarioSeguridad() {
        return seguridad.getProperty("driver.seguridad.usuario");
    }

    public static String getDriverClaveSeguridad() {
        return seguridad.getProperty("driver.seguridad.clave");
    }

    public static String getDriverUrlCampeonato() {
        return campeonato.getProperty("driver.campeonato.url");
    }

    public static String getDriverUsuarioCampeonato() {
        return campeonato.getProperty("driver.campeonato.usuario");
    }

    public static String getDriverClaveCampeonato() {
        return campeonato.getProperty("driver.campeonato.clave");
    }

    public static String getDriverUrlEstadisticas() {
        return estadisticas.getProperty("driver.estadisticas.url");
    }

    public static String getDriverUsuarioEstadisticas() {
        return estadisticas.getProperty("driver.estadisticas.usuario");
    }

    public static String getDriverClaveEstadisticas() {
        return estadisticas.getProperty("driver.estadisticas.clave");
    }
}