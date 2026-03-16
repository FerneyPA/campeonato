package com.campeonato.menu;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.campeonato.modelo.ui.MenuItem;

@Named
@SessionScoped
public class MenuCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<MenuItem> items = Collections.emptyList();

    // ============================================================
    // Se llama desde LoginBusiness al autenticar exitosamente
    // ============================================================
    public void cargar(List<MenuItem> items) {
        this.items = items != null ? items : Collections.emptyList();
    }

    public void limpiar() {
        this.items = Collections.emptyList();
    }

    // ============================================================
    // Getter — disponible en template.xhtml como #{menuCache.items}
    // ============================================================
    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}