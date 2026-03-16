package com.campeonato.modelo.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String       id;
    private final String       nombre;
    private final String       url;
    private final int          orden;
    private final List<MenuItem> hijos = new ArrayList<>();

    // ============================================================
    // Constructor
    // ============================================================
    public MenuItem(String id, String nombre, String url, int orden) {
        this.id     = id;
        this.nombre = nombre;
        this.url    = url;
        this.orden  = orden;
    }

    // ============================================================
    // Un nodo es agrupador si no tiene URL — solo agrupa hijos
    // Un nodo es item si tiene URL — es una opción navegable
    // ============================================================
    public boolean esAgrupador() {
        return url == null || url.trim().isEmpty();
    }

    public boolean tieneHijos() {
        return !hijos.isEmpty();
    }

    public void agregarHijo(MenuItem hijo) {
        hijos.add(hijo);
    }

    // ============================================================
    // Getters
    // ============================================================
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public int getOrden() {
        return orden;
    }

    public List<MenuItem> getHijos() {
        return Collections.unmodifiableList(hijos);
    }
}