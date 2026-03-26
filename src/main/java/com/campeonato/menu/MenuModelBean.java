package com.campeonato.menu;

import com.campeonato.modelo.ui.MenuItem;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import java.io.Serializable;

@Named
@SessionScoped
public class MenuModelBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private MenuCache menuCache;

    private MenuModel modelo;

    // ============================================================
    // Inicializa el menú al iniciar la sesión del usuario
    // ============================================================
    @PostConstruct
    public void init() {
        construir();
    }

    // ============================================================
    // Construye el modelo PrimeFaces desde el árbol de MenuItem
    // ============================================================
    public void construir() {
        modelo = new DefaultMenuModel();

        if (menuCache == null || menuCache.getItems() == null) {
            return;
        }

        for (MenuItem categoria : menuCache.getItems()) {

            if (categoria == null) continue;

            // NIVEL 1 — Categoría principal
            DefaultSubMenu subMenuCategoria = DefaultSubMenu.builder()
                    .label(categoria.getNombre())
                    .build();

            for (MenuItem columna : categoria.getHijos()) {

                if (columna == null) continue;

                // NIVEL 2 — Submenú dentro de categoría
                DefaultSubMenu subMenuColumna = DefaultSubMenu.builder()
                        .label(columna.getNombre())
                        .build();

                for (MenuItem item : columna.getHijos()) {

                    if (item == null || item.isAgrupador()) continue;

                    // NIVEL 3 — Items finales
                    DefaultMenuItem menuItem = DefaultMenuItem.builder()
                            .value(item.getNombre())
                            .url(item.getUrl())
                            .build();

                    subMenuColumna.getElements().add(menuItem);
                }

                subMenuCategoria.getElements().add(subMenuColumna);
            }

            modelo.getElements().add(subMenuCategoria);
        }

    }

    // ============================================================
    // Reconstruye el menú después del login
    // ============================================================
    public void refrescar() {
        construir();
    }

    public MenuModel getModelo() {
        return modelo;
    }
}