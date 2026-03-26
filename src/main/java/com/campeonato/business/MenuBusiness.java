package com.campeonato.business;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.campeonato.modelo.MenuItemDto;
import com.campeonato.modelo.ui.MenuItem;

import jakarta.ejb.Stateless;

@Stateless
public class MenuBusiness
{

    public List<MenuItem> construirArbol(List<MenuItemDto> nodos)
    {

        Map<String, MenuItem> mapa = new LinkedHashMap<>();
        for (MenuItemDto dto : nodos)
        {
            mapa.put(dto.getId(), new MenuItem(dto.getId(), dto.getNombre(), dto.getUrl(), dto.getOrden()));
        }

        List<MenuItem> raices = new ArrayList<>();

        for (MenuItemDto dto : nodos)
        {
            MenuItem nodo    = mapa.get(dto.getId());
            String   padreId = dto.getPadreId();

            if (padreId == null)
            {
                raices.add(nodo);
            } else
            {
                MenuItem padre = mapa.get(padreId);
                if (padre != null)
                {
                    padre.agregarHijo(nodo);
                }
            }
        }

        return raices;
    }
}