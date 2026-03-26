package com.campeonato.modelo.ui;

import jakarta.faces.model.SelectItem;

public class ReferenciaItem
{

    private final String id;
    private final String nombre;

    public ReferenciaItem(String id, String nombre)
    {
        this.id     = id;
        this.nombre = nombre;
    }

    public String getId()
    {
        return id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public SelectItem toSelectItem()
    {
        return new SelectItem(id, nombre);
    }

    @Override
    public String toString()
    {
        return "[" + id + "] " + nombre;
    }
}