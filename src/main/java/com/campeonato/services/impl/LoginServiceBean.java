package com.campeonato.services.impl;

import com.campeonato.beans.UsuarioSesion;
import com.campeonato.business.LoginBusiness;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.menu.MenuCache;
import com.campeonato.services.AbstractLoginService;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class LoginServiceBean extends AbstractLoginService {

    @Inject
    private LoginBusiness loginBusiness;

    @Override
    public boolean login(String username, String password,
                         UsuarioSesion usuarioSesion) {
        logInicioLogin(username);
        try {
            // MenuCache se inyecta aquí porque el Service tiene acceso a CDI
            // y puede obtener el bean de sesión correcto
            boolean resultado = loginBusiness.validarCredenciales(
                username, password, usuarioSesion, getMenuCache()
            );
            logFinLogin(username, resultado);
            return resultado;
        } catch (Exception e) {
            logError(username, e);
            throw e;
        }
    }

    @Override
    public void logout(String username, UsuarioSesion usuarioSesion) {
        logInicioLogout(username);
        usuarioSesion.cerrarSesion();
        getMenuCache().limpiar();
        logFinLogout(username);
    }

    @Inject
    private MenuCache menuCache;

    private MenuCache getMenuCache() {
        return menuCache;
    }
}