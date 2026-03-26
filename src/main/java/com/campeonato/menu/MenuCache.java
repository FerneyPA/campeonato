package com.campeonato.menu;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.campeonato.modelo.ui.MenuItem;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class MenuCache implements Serializable
{

    private static final long serialVersionUID = 1L;

    private List<MenuItem> items = Collections.emptyList();

    public void cargar(List<MenuItem> items)
    {
        this.items = items != null ? items : Collections.emptyList();
    }

    public List<MenuItem> getItems()
    {
        return Collections.unmodifiableList(items);
    }

    public void limpiar()
    {
        items = Collections.emptyList();
    }
}