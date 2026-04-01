package com.campeonato.exception;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class MensajesManager {
    private static final String ARCHIVO_MENSAJE = "mensajes";
    private static final String ARCHIVO_ERROR = "errores";

    // Control para cargar propiedades en UTF-8 con fallback a ISO-8859-1
    private static final ResourceBundle.Control UTF8_CONTROL = new ResourceBundle.Control() {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
                boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {

            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");

            // Intento de lectura en UTF-8
            PropertyResourceBundle bundle = null;

            InputStream stream = null;
            URL resourceUrl = loader.getResource(resourceName);

            if (resourceUrl != null) {
                URLConnection connection = resourceUrl.openConnection();
                if (connection != null) {
                    connection.setUseCaches(!reload);
                    stream = connection.getInputStream();
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }

            if (stream != null) {
                try (InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    bundle = new PropertyResourceBundle(isr);
                }

                // Si el bundle contiene caracteres de reemplazo (�), es probable que el archivo
                // esté en ISO-8859-1/CP1252; reintentar la lectura en ISO-8859-1
                boolean hasReplacement = false;
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        String val = bundle.getString(key);
                        if (val.indexOf('\uFFFD') >= 0) {
                            hasReplacement = true;
                            break;
                        }
                    }
                }

                if (hasReplacement) {
                    // Reabrir el stream y leer con ISO-8859-1
                    InputStream stream2 = null;
                    if (resourceUrl != null) {
                        URLConnection connection2 = resourceUrl.openConnection();
                        if (connection2 != null) {
                            connection2.setUseCaches(!reload);
                            stream2 = connection2.getInputStream();
                        }
                    } else {
                        stream2 = loader.getResourceAsStream(resourceName);
                    }

                    if (stream2 != null) {
                        try (InputStreamReader isr2 = new InputStreamReader(stream2, StandardCharsets.ISO_8859_1)) {
                            bundle = new PropertyResourceBundle(isr2);
                        }
                    }
                }
            }

            return bundle;
        }
    };

    // ============================================================
    // Mensajes informativos — mensajes.properties
    // ============================================================
    public static String getMensaje(String codigo) {
        return getMensaje(codigo, Locale.getDefault());
    }

    public static String getMensaje(String codigo, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(ARCHIVO_MENSAJE, locale, UTF8_CONTROL);
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
            ResourceBundle bundle = ResourceBundle.getBundle(ARCHIVO_ERROR, locale, UTF8_CONTROL);
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