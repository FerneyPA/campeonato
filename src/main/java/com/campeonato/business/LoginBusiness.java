package com.campeonato.business;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.campeonato.connection.CampeonatoConnection;
import com.campeonato.connection.ConnectionManager;
import com.campeonato.dao.MenuDAO;
import com.campeonato.dao.UsuarioDAO;
import com.campeonato.dao.registro.DAOFactory;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.inicio.beans.UsuarioSesion;
import com.campeonato.menu.MenuCache;
import com.campeonato.modelo.MenuItemDto;
import com.campeonato.modelo.ui.MenuItem;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class LoginBusiness {

	private static final Logger log = LogManager.getLogger(LoginBusiness.class);

	@Inject
	private ConnectionManager connectionManager;

	@Inject
	private MenuBusiness menuBusiness;

	public boolean validarCredenciales(String username, String clave, UsuarioSesion usuarioSesion,
			MenuCache menuCache) {


		CampeonatoConnection conn = null;

		try {
			conn = connectionManager.getConnectionSeguridad();

			UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO(conn);
			MenuDAO menuDAO = DAOFactory.getMenuDAO(conn);

			boolean credencialesOk = usuarioDAO.existeUsuario(username, clave);

			if (!credencialesOk) {
				throw new CampeonatoException(CampeonatoException.BUS_CREDENCIALES);
			}

			String rol = usuarioDAO.getRolUsuario(username);

			Map<String, Set<String>> operacionesPorUrl = usuarioDAO.getOperacionesPorUrl(username);

			List<MenuItemDto> nodos = menuDAO.getNodosMenu(username);

			List<MenuItem> arbol = menuBusiness.construirArbol(nodos);

			usuarioSesion.iniciarSesion(username, rol, operacionesPorUrl);
			menuCache.cargar(arbol);

			return true;

		} catch (CampeonatoException e) {
			if (conn != null) {
				conn.error();
			}
			log.error("[LoginBusiness] Error al validar credenciales para: {} — {}", username, e.toString());
			throw e;

		} catch (Exception e) {
			if (conn != null) {
				conn.error();
			}
			log.error("[LoginBusiness] Error inesperado en validarCredenciales", e);
			throw new CampeonatoException(CampeonatoException.BUS_PROCESO, e);

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}