package com.campeonato.business;

import java.util.List;

import org.slf4j.Logger;

import com.campeonato.connection.CampeonatoConnection;
import com.campeonato.connection.ConnectionManager;
import com.campeonato.dao.EquipoDAO;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.modelo.Equipo;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class EquipoBusiness
{

    private static final Logger log = LogManager.getLogger(EquipoBusiness.class);

    @Inject
    private ConnectionManager connectionManager;


    public List<Equipo> buscar(String filtro) throws CampeonatoException
    {
        String metodo = "buscar";
        CampeonatoConnection cc;

        try
        {

            cc = connectionManager.getConnectionCampeonato();
            EquipoDAO dao = new EquipoDAO(cc.getConnection());
            return dao.buscar(filtro);
        } catch (CampeonatoException e)
        {
            throw e;
        } catch (Exception e)
        {
            throw new CampeonatoException("BUS-001" + metodo, e);
        }
    }


    public void eliminar(Integer idEquipo) throws CampeonatoException
    {
        String metodo = "eliminar";
        CampeonatoConnection cc;

        try
        {

            cc = connectionManager.getConnectionCampeonato();
            EquipoDAO dao = new EquipoDAO(cc.getConnection());
            dao.eliminar(idEquipo);
        } catch (CampeonatoException e)
        {
            throw e;
        } catch (Exception e)
        {
            throw new CampeonatoException("BUS-005" + metodo, e);
        }
    }


    public void guardar(Equipo equipo) throws CampeonatoException
    {
        String metodo = "guardar";
        CampeonatoConnection cc;

        try
        {

            cc = connectionManager.getConnectionCampeonato();
            EquipoDAO dao = new EquipoDAO(cc.getConnection());
            if (equipo.getIdEquipo() == null)
            {
                dao.insertar(equipo);
            } else
            {
                dao.actualizar(equipo);
            }
        } catch (CampeonatoException e)
        {
            throw e;
        } catch (Exception e)
        {
            throw new CampeonatoException("BUS-003" + metodo, e);
        }
    }


    public List<Equipo> listar() throws CampeonatoException
    {
        String metodo = "listar";
        CampeonatoConnection cc;

        try
        {

            cc = connectionManager.getConnectionCampeonato();

            EquipoDAO dao = new EquipoDAO(cc.getConnection());
            return dao.listar();
        } catch (CampeonatoException e)
        {
            throw e;
        } catch (Exception e)
        {
            throw new CampeonatoException("BUS-001" + metodo, e);
        }
    }


    public Equipo obtener(Integer idEquipo) throws CampeonatoException
    {
        String metodo = "obtener";
        CampeonatoConnection cc;

        try
        {

            cc = connectionManager.getConnectionCampeonato();
            EquipoDAO dao = new EquipoDAO(cc.getConnection());
            return dao.obtener(idEquipo);
        } catch (CampeonatoException e)
        {
            throw e;
        } catch (Exception e)
        {
            throw new CampeonatoException("BUS-002" + metodo, e);
        }
    }
}