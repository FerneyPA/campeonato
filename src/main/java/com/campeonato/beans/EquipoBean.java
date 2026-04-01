package com.campeonato.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.campeonato.business.EquipoBusiness;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.modelo.Equipo;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class EquipoBean extends BaseBean implements Serializable
{

    private static final long serialVersionUID = 1L;

    @EJB
    private EquipoBusiness equipoBusiness;

    private List<Equipo> lista;
    private String       filtro;
    private Equipo       equipoSeleccionado;
    private Equipo       equipo;
    private Integer      idEquipoParam;

    public void buscar()
    {
        try
        {
            if (filtro == null || filtro.isBlank())
            {
                lista = equipoBusiness.listar();
            } else
            {
                lista = equipoBusiness.buscar(filtro);
            }
        } catch (CampeonatoException e)
        {
            mensajeError(e);
        }
    }

    public String cancelar()
    {
        return "/equipos/lista?faces-redirect=true";
    }

    private void cargarLista()
    {
            lista = equipoBusiness.listar();
    }

    // Cambiado para devolver String (null) y usar clave i18n
    public String eliminar(Equipo equipo)
    {
        try
        {
            equipoBusiness.eliminar(equipo.getIdEquipo());
            mensajeInfo("info.eliminado");
            cargarLista();
            return null;
        } catch (CampeonatoException e)
        {
            mensajeError(e);
            return null;
        }
    }

    public Equipo getEquipo()
    {
        return equipo;
    }

    public Equipo getEquipoSeleccionado()
    {
        return equipoSeleccionado;
    }

    public String getFiltro()
    {
        return filtro;
    }

    public Integer getIdEquipoParam()
    {
        return idEquipoParam;
    }

    public List<Equipo> getLista()
    {
        return lista;
    }

    public String guardar()
    {
        try
        {
            equipoBusiness.guardar(equipo);
            mensajeInfo("info.guardado");
            return "/equipos/lista?faces-redirect=true";
        } catch (CampeonatoException e)
        {
            mensajeError(e);
            return null;
        }
    }

    @PostConstruct
    public void init()
    {
        lista = new ArrayList<>(); // inicializar antes de la llamada al negocio
        try {
            cargarLista();
        } catch (Exception e) {
            mensajeError("Error al cargar equipos");
        }
    }

    public void initForm()
    {
        if (idEquipoParam != null)
        {
            try
            {
                equipo = equipoBusiness.obtener(idEquipoParam);
            } catch (CampeonatoException e)
            {
                mensajeError(e);
            }
        } else
        {
            equipo = new Equipo();
        }
    }

    public String irEditar(Equipo equipo)
    {
        return "/equipos/form?faces-redirect=true&idEquipo=" + equipo.getIdEquipo();
    }

    public String irNuevo()
    {
        return "/equipos/form?faces-redirect=true";
    }

    public void limpiar()
    {
        filtro = null;
        cargarLista();
    }

    public void setEquipo(Equipo equipo)
    {
        this.equipo = equipo;
    }

    public void setEquipoSeleccionado(Equipo equipoSeleccionado)
    {
        this.equipoSeleccionado = equipoSeleccionado;
    }

    public void setFiltro(String filtro)
    {
        this.filtro = filtro;
    }

    public void setIdEquipoParam(Integer idEquipoParam)
    {
        this.idEquipoParam = idEquipoParam;
    }
}