package com.campeonato.modelo.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuItem implements Serializable
{

    private static final long serialVersionUID = 1L;

    private final String         id;
    private final String         nombre;
    private final String         url;
    private final int            orden;
    private final List<MenuItem> hijos = new ArrayList<>();

    public MenuItem(String id, String nombre, String url, int orden)
    {
        this.id     = id;
        this.nombre = nombre;
        this.url    = url;
        this.orden  = orden;
    }

    public void agregarHijo(MenuItem hijo)
    {
        hijos.add(hijo);
    }

    public boolean isAgrupador()
    {
        return url == null || url.trim().isEmpty();
    }

    public List<MenuItem> getHijos()
    {
        return Collections.unmodifiableList(hijos);
    }

    public String getId()
    {
        return id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public int getOrden()
    {
        return orden;
    }

    public String getUrl()
    {
        return url;
    }

    public boolean isTieneHijos()
    {
        return !hijos.isEmpty();
    }
}