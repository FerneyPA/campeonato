package com.campeonato.exception;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MensajesManager {

    private static final String ARCHIVO_BASE = "mensajes";

    // ============================================================
    // Obtiene el mensaje según el código y el idioma activo
    // Si no encuentra el mensaje en el idioma activo cae al
    // archivo base (español)
    // ============================================================

    public static String getMensaje(String codigo) {
        return getMensaje(codigo, Locale.getDefault());
    }

    public static String getMensaje(String codigo, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(ARCHIVO_BASE, locale);
            return bundle.getString(codigo);
        } catch (MissingResourceException e) {
            return "[" + codigo + "] Mensaje no disponible.";
        }
    }

    // ============================================================
    // Obtiene el mensaje de una CampeonatoException
    // usando su código como clave en el archivo de propiedades
    // ============================================================

    public static String getMensaje(CampeonatoException e) {
        return getMensaje(e.getCodigo());
    }

    public static String getMensaje(CampeonatoException e, Locale locale) {
        return getMensaje(e.getCodigo(), locale);
    }
}