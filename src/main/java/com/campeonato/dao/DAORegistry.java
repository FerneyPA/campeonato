package com.campeonato.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DAORegistry {

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private ReferenciasDAO referenciasDAO;

    @Inject
    private MenuDAO menuDAO;

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public ReferenciasDAO getReferenciasDAO() {
        return referenciasDAO;
    }

    public MenuDAO getMenuDAO() {
        return menuDAO;
    }
}