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

    public void construir()
    {
        String contextPath = servletContext.getContextPath();
        modelo = new DefaultMenuModel();
        if (menuCache == null || menuCache.getItems() == null)
        {
            return;
        }
        for (MenuItem categoria : menuCache.getItems())
        {
            if (categoria == null)
            {
                continue;
            }
            // NIVEL 1 — Categoría principal
            DefaultSubMenu subMenuCategoria = DefaultSubMenu.builder().label(categoria.getNombre()).build();
            for (MenuItem columna : categoria.getHijos())
            {
                if (columna == null)
                {
                    continue;
                }
                // NIVEL 2 — puede ser submenú o ítem navegable directamente
                if (columna.isAgrupador())
                {
                    // Tiene hijos → crear submenú normal
                    DefaultSubMenu subMenuColumna = DefaultSubMenu.builder().label(columna.getNombre()).build();
                    for (MenuItem item : columna.getHijos())
                    {
                        if (item == null || item.isAgrupador())
                        {
                            continue;
                        }
                        // NIVEL 3 — Items finales
                        DefaultMenuItem menuItem = DefaultMenuItem.builder().value(item.getNombre())
                                .url(contextPath + item.getUrl()).build();
                        subMenuColumna.getElements().add(menuItem);
                    }
                    subMenuCategoria.getElements().add(subMenuColumna);
                } else
                {
                    // No tiene hijos → es navegable directo desde nivel 2
                    DefaultMenuItem menuItem = DefaultMenuItem.builder().value(columna.getNombre())
                            .url(contextPath + columna.getUrl()).build();
                    subMenuCategoria.getElements().add(menuItem);
                }
            }
            modelo.getElements().add(subMenuCategoria);
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