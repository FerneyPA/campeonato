package com.campeonato.modelo.ui;

import jakarta.faces.model.SelectItem;

public class ReferenciaItem {

    private final String id;
    private final String nombre;

    // ============================================================
    // Constructor
    // ============================================================
    public ReferenciaItem(String id, String nombre) {
        this.id     = id;
        this.nombre = nombre;
    }

    // ============================================================
    // Convierte a SelectItem de JSF — para usar directamente
    // en componentes de selección de PrimeFaces
    //
    // Uso en XHTML:
    //   <f:selectItems value="#{referenciasCache.tiposDocumento}"
    //                  var="item"
    //                  itemValue="#{item.id}"
    //                  itemLabel="#{item.nombre}" />
    // ============================================================
    public SelectItem toSelectItem() {
        return new SelectItem(id, nombre);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre;
    }
}