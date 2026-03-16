package com.campeonato.modelo;

public class MenuItemDto {

    private final String id;
    private final String nombre;
    private final String url;
    private final String padreId;
    private final int    orden;

    // ============================================================
    // Constructor — recibe los datos crudos de la BD
    // incluyendo padre_id para que MenuBusiness arme el árbol
    // ============================================================
    public MenuItemDto(String id, String nombre, String url,
                       String padreId, int orden) {
        this.id      = id;
        this.nombre  = nombre;
        this.url     = url;
        this.padreId = padreId;
        this.orden   = orden;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public String getPadreId() {
        return padreId;
    }

    public int getOrden() {
        return orden;
    }
}