package com.campeonato.services;

import com.campeonato.exception.LogManager;
import com.campeonato.inicio.beans.UsuarioSesion;
import com.campeonato.menu.MenuCache;
import com.campeonato.menu.MenuModelBean;
import com.campeonato.business.LoginBusiness;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import org.slf4j.Logger;

@Stateless
public class AbstractLoginService implements ILoginService
{

    private static final Logger log = LogManager.getLogger(AbstractLoginService.class);

    @Inject
    private LoginBusiness loginBusiness;

    @Inject
    private MenuCache menuCache;

    @Inject
    private MenuModelBean menuModelBean;

    // ============================================================
    // Login — loguea entrada/salida y delega al Business
    // Después del login reconstruye el MenuModel con los datos
    // del usuario autenticado
    // ============================================================
    @Override
    public boolean login(String username, String password, UsuarioSesion usuarioSesion)
    {

        try
        {
            boolean resultado = loginBusiness.validarCredenciales(
                username, password, usuarioSesion, menuCache
            );

            if (resultado)
            {
                menuModelBean.refrescar();
            }

            return resultado;

        } catch (Exception e)
        {
            logError(username, e);
            throw e;
        }
    }

    // ============================================================
    // Logout — loguea entrada/salida y limpia la sesión
    // ============================================================
    @Override
    public void logout(String username, UsuarioSesion usuarioSesion)
    {
        usuarioSesion.cerrarSesion();
        menuCache.limpiar();
        menuModelBean.refrescar();
    }

    // ============================================================
    // Métodos de logging
    // ============================================================

    private void logInicioLogin(String username)
    {
        log.info("[SERVICE] Inicio de login para usuario: {}", username);
    }

    private void logFinLogin(String username, boolean exitoso)
    {
        if (exitoso)
        {
            log.info("[SERVICE] Login exitoso para usuario: {}", username);
        } else
        {
            log.warn("[SERVICE] Login fallido para usuario: {}", username);
        }
    }

    private void logInicioLogout(String username)
    {
        log.info("[SERVICE] Inicio de logout para usuario: {}", username);
    }

    private void logFinLogout(String username)
    {
        log.info("[SERVICE] Logout completado para usuario: {}", username);
    }

    private void logError(String username, Exception e)
    {
        log.error("[SERVICE] Error en proceso de login para usuario: {} — {}", username, e.getMessage(), e);
    }
}