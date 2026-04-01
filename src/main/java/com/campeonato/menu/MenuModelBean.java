package com.campeonato.menu;

import java.io.Serializable;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.campeonato.modelo.ui.MenuItem;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@SessionScoped
public class MenuModelBean implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Inject
    private MenuCache menuCache;

    @Inject
    private jakarta.servlet.ServletContext servletContext;

    private MenuModel modelo;

    public void construir() {
        String contextPath = servletContext.getContextPath();
        modelo = new DefaultMenuModel();
        if (menuCache == null || menuCache.getItems() == null) {
            return;
        }
        for (MenuItem categoria : menuCache.getItems()) {
            if (categoria == null) continue;
            // NIVEL 1 — siempre SubMenu
            DefaultSubMenu subMenuCategoria = DefaultSubMenu.builder().label(categoria.getNombre()).build();
            agregarHijos(subMenuCategoria, categoria, contextPath);
            modelo.getElements().add(subMenuCategoria);
        }
    }

    private void agregarHijos(DefaultSubMenu padre, MenuItem nodo, String contextPath) {
        for (MenuItem hijo : nodo.getHijos()) {
            if (hijo == null) continue;
            if (hijo.isAgrupador()) {
                // Tiene hijos → crear submenú y recursar
                DefaultSubMenu subMenu = DefaultSubMenu.builder().label(hijo.getNombre()).build();
                agregarHijos(subMenu, hijo, contextPath);
                padre.getElements().add(subMenu);
            } else {
                // Es navegable → ítem final
                DefaultMenuItem menuItem = DefaultMenuItem.builder()
                    .value(hijo.getNombre())
                    .url(contextPath + hijo.getUrl())
                    .build();
                padre.getElements().add(menuItem);
            }
        }
    }

    public MenuModel getModelo()
    {
        return modelo;
    }

    @PostConstruct
    public void init()
    {
        construir();
    }

    public void refrescar()
    {
        construir();
    }
}