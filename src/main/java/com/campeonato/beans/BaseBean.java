package com.campeonato.beans;

import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.exception.MensajesManager;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;

import java.io.Serializable;

public abstract class BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(BaseBean.class);

    // ============================================================
    // Mensajes a la vista — clave + uno o varios parámetros
    // para reemplazar los {} del mensaje
    //
    // Uso con un parámetro:
    //   mensajeInfo("info.bienvenida", username);
    //   → "Bienvenido, admin"
    //
    // Uso con varios parámetros:
    //   mensajeWarn("warn.intentos", intentos, maximo);
    //   → "Llevas 3 intentos fallidos de 5 permitidos"
    // ============================================================

    protected void mensajeError(String clave, Object... parametros) {
        agregarMensaje(FacesMessage.SEVERITY_ERROR, formatear(clave, parametros));
    }

    protected void mensajeError(CampeonatoException e) {
        agregarMensaje(FacesMessage.SEVERITY_ERROR, MensajesManager.getMensaje(e));
    }

    protected void mensajeInfo(String clave, Object... parametros) {
        agregarMensaje(FacesMessage.SEVERITY_INFO, formatear(clave, parametros));
    }

    protected void mensajeWarn(String clave, Object... parametros) {
        agregarMensaje(FacesMessage.SEVERITY_WARN, formatear(clave, parametros));
    }

    // ============================================================
    // Logging — clave + uno o varios parámetros
    // Si el último parámetro es una excepción SLF4J agrega
    // el stack trace automáticamente
    // ============================================================

    protected void logInfo(String clave, Object... parametros) {
        log.info(MensajesManager.getMensaje(clave), parametros);
    }

    protected void logWarn(String clave, Object... parametros) {
        log.warn(MensajesManager.getMensaje(clave), parametros);
    }

    protected void logError(String clave, Object... parametros) {
        log.error(MensajesManager.getMensaje(clave), parametros);
    }

    // ============================================================
    // Acceso a objetos HTTP
    // ============================================================

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    protected HttpServletRequest getRequest() {
        return (HttpServletRequest) getFacesContext()
                .getExternalContext().getRequest();
    }

    protected HttpServletResponse getResponse() {
        return (HttpServletResponse) getFacesContext()
                .getExternalContext().getResponse();
    }

    protected HttpSession getSession() {
        return (HttpSession) getFacesContext()
                .getExternalContext().getSession(false);
    }

    protected void invalidarSesion() {
        HttpSession session = getSession();
        if (session != null) {
            session.invalidate();
        }
    }

    // ============================================================
    // Método interno — reemplaza los {} del mensaje con
    // los parámetros en orden
    // ============================================================

    private String formatear(String clave, Object... parametros) {
        String mensaje = MensajesManager.getMensaje(clave);
        for (Object parametro : parametros) {
            mensaje = mensaje.replaceFirst("\\{\\}", parametro != null ? parametro.toString() : "");
        }
        return mensaje;
    }

    private void agregarMensaje(FacesMessage.Severity severidad, String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(severidad, mensaje, null));
    }
}