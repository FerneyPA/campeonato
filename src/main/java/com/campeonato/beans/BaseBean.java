package com.campeonato.beans;

import java.io.Serializable;

import org.slf4j.Logger;

import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.exception.MensajesManager;
import com.campeonato.inicio.beans.UsuarioSesion;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public abstract class BaseBean implements Serializable
{

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(BaseBean.class);

    @Inject
    private UsuarioSesion usuarioSesion;

    
    private void agregarMensaje(FacesMessage.Severity severidad, String mensaje) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().getFlash().setKeepMessages(true);
        ctx.addMessage(null, new FacesMessage(severidad, mensaje, null));
    }

    private String formatear(String clave, Object... parametros)
    {
        String mensaje = MensajesManager.getMensaje(clave);
        for (Object parametro : parametros)
        {
            mensaje = mensaje.replaceFirst("\\{\\}", parametro != null ? parametro.toString() : "");
        }
        return mensaje;
    }

    protected FacesContext getFacesContext()
    {
        return FacesContext.getCurrentInstance();
    }

    protected HttpServletRequest getRequest()
    {
        return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
    }

    protected HttpServletResponse getResponse()
    {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }

    protected HttpSession getSession()
    {
        return (HttpSession) getFacesContext().getExternalContext().getSession(false);
    }

    protected void invalidarSesion()
    {
        HttpSession session = getSession();
        if (session != null)
        {
            session.invalidate();
        }
    }

    protected void logError(String clave, Object... parametros)
    {
        log.error(MensajesManager.getMensaje(clave), parametros);
    }

    protected void logInfo(String clave, Object... parametros)
    {
        log.info(MensajesManager.getMensaje(clave), parametros);
    }

    protected void logWarn(String clave, Object... parametros)
    {
        log.warn(MensajesManager.getMensaje(clave), parametros);
    }

    protected void mensajeError(CampeonatoException e)
    {
        agregarMensaje(FacesMessage.SEVERITY_ERROR, MensajesManager.getMensaje(e));
    }

    protected void mensajeError(String clave, Object... parametros)
    {
        agregarMensaje(FacesMessage.SEVERITY_ERROR, formatear(clave, parametros));
    }

    protected void mensajeInfo(String clave, Object... parametros)
    {
        agregarMensaje(FacesMessage.SEVERITY_INFO, formatear(clave, parametros));
    }

    protected void mensajeWarn(String clave, Object... parametros)
    {
        agregarMensaje(FacesMessage.SEVERITY_WARN, formatear(clave, parametros));
    }

    protected boolean puedeConsultar()
    {
        return tieneOperacion("CONSULTAR");
    }

    protected boolean puedeEditar()
    {
        return tieneOperacion("EDITAR");
    }

    protected boolean puedeExportar()
    {
        return tieneOperacion("EXPORTAR");
    }

    protected boolean puedeGuardar()
    {
        return tieneOperacion("GUARDAR");
    }

    protected boolean puedeVer()
    {
        return tieneOperacion("VER");
    }

    private boolean tieneOperacion(String operacion)
    {
        String url = getRequest().getServletPath();
        return usuarioSesion.tieneOperacion(url, operacion);
    }
}