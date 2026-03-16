package com.campeonato.business;

import com.campeonato.modelo.MenuItemDto;
import com.campeonato.modelo.ui.MenuItem;

import jakarta.ejb.Stateless;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class MenuBusiness {

    // ============================================================
    // Arma el árbol recursivo de menú a partir de la lista plana
    // que devuelve MenuDAO
    //
    // Algoritmo:
    // 1. Carga todos los nodos en un mapa id → MenuItem
    // 2. Recorre el mapa y asigna cada nodo a su padre
    // 3. Los nodos sin padre son los nodos raíz — se retornan
    // ============================================================
    public List<MenuItem> construirArbol(List<MenuItemDto> nodos) {

        // Mapa id → MenuItem para acceso O(1)
        Map<String, MenuItem> mapa = new LinkedHashMap<>();
        for (MenuItemDto dto : nodos) {
            mapa.put(dto.getId(), new MenuItem(
                dto.getId(),
                dto.getNombre(),
                dto.getUrl(),
                dto.getOrden()
            ));
        }

        // Segunda pasada — asignar hijos a sus padres
        List<MenuItem> raices = new ArrayList<>();

        for (MenuItemDto dto : nodos) {
            MenuItem nodo    = mapa.get(dto.getId());
            String  padreId  = dto.getPadreId();

            if (padreId == null) {
                // Sin padre → nodo raíz
                raices.add(nodo);
            } else {
                // Con padre → agregar como hijo
                MenuItem padre = mapa.get(padreId);
                if (padre != null) {
                    padre.agregarHijo(nodo);
                }
            }
        }

        return raices;
    }
}