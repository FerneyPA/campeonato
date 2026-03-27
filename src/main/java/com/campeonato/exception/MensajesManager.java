package com.campeonato.exception;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
public class MensajesManager {
    private static final String ARCHIVO_MENSAJE = "mensajes";
    private static final String ARCHIVO_ERROR = "errores";

    // ============================================================
    // Mensajes informativos — mensajes.properties
    // ============================================================
    public static String getMensaje(String codigo) {
        return getMensaje(codigo, Locale.getDefault());
    }
    public static String getMensaje(String codigo, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(ARCHIVO_MENSAJE, locale);
            return bundle.getString(codigo);
        } catch (MissingResourceException e) {
            return "[" + codigo + "] Mensaje no disponible.";
        }
    }

    // ============================================================
    // Errores — errores.properties
    // ============================================================
    public static String getError(String codigo) {
        return getError(codigo, Locale.getDefault());
    }
    public static String getError(String codigo, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(ARCHIVO_ERROR, locale);
            return bundle.getString(codigo);
        } catch (MissingResourceException e) {
            return "[" + codigo + "] Mensaje no disponible.";
        }
    }

    // ============================================================
    // Obtiene el error de una CampeonatoException
    // ============================================================
    public static String getMensaje(CampeonatoException e) {
        return getError(e.getCodigo());
    }
    public static String getMensaje(CampeonatoException e, Locale locale) {
        return getError(e.getCodigo(), locale);
    }
}