package com.campeonato.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Equipo implements Serializable
{

    private static final long serialVersionUID = 1L;

    private Integer   idEquipo;
    private String    nombre;
    private String    ciudad;
    private LocalDate fechaFundacion;

    public Equipo()
    {
    }

    public Equipo(Integer idEquipo, String nombre, String ciudad, LocalDate fechaFundacion)
    {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.fechaFundacion = fechaFundacion;
    }

    public String getCiudad()
    {
        return ciudad;
    }

    public LocalDate getFechaFundacion()
    {
        return fechaFundacion;
    }

    public Integer getIdEquipo()
    {
        return idEquipo;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setCiudad(String ciudad)
    {
        this.ciudad = ciudad;
    }

    public void setFechaFundacion(LocalDate fechaFundacion)
    {
        this.fechaFundacion = fechaFundacion;
    }

    public void setIdEquipo(Integer idEquipo)
    {
        this.idEquipo = idEquipo;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
}