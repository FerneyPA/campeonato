package com.campeonato.business;

import com.campeonato.beans.UsuarioSesion;
import com.campeonato.connection.CampeonatoConnection;
import com.campeonato.connection.ConnectionManager;
import com.campeonato.dao.DAORegistry;
import com.campeonato.dao.MenuDAO;
import com.campeonato.dao.UsuarioDAO;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.menu.MenuCache;
import com.campeonato.modelo.MenuItemDto;
import com.campeonato.modelo.ui.MenuItem;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

@Stateless
public class LoginBusiness {

    private static final Logger log = LogManager.getLogger(LoginBusiness.class);

    @Inject
    private ConnectionManager connectionManager;

    @Inject
    private DAORegistry dao;

    @Inject
    private MenuBusiness menuBusiness;

    // ============================================================
    // Valida credenciales, carga rol, URLs permitidas y menú
    // ============================================================
    public boolean validarCredenciales(String username, String clave,
                                        UsuarioSesion usuarioSesion,
                                        MenuCache menuCache) {

        LogManager.inicio(log, "validarCredenciales");

        CampeonatoConnection conn = null;

        try {
            conn = connectionManager.getConnectionSeguridad();
            UsuarioDAO usuarioDAO = dao.getUsuarioDAO();
            MenuDAO    menuDAO    = dao.getMenuDAO();

            // 1. Validar credenciales
            boolean credencialesOk = usuarioDAO.existeUsuario(conn.get(), username, clave);

            if (!credencialesOk) {
                log.warn("[LoginBusiness] Credenciales inválidas para usuario: {}", username);
                return false;
            }

            // 2. Cargar rol
            String rol = usuarioDAO.getRolUsuario(conn.get(), username);

            // 3. Cargar URLs permitidas
            Set<String> urlsPermitidas = usuarioDAO.getUrlsPermitidas(conn.get(), username);

            // 4. Consultar nodos planos del menú desde BD
            List<MenuItemDto> nodos = menuDAO.getNodosMenu(conn.get(), username);

            // 5. Armar árbol recursivo — responsabilidad del Business
            List<MenuItem> arbol = menuBusiness.construirArbol(nodos);

            // 6. Iniciar sesión con todos los datos
            usuarioSesion.iniciarSesion(username, rol, urlsPermitidas);
            menuCache.cargar(arbol);

            LogManager.fin(log, "validarCredenciales");
            return true;

        } catch (CampeonatoException e) {
            if (conn != null) conn.error();
            log.error("[LoginBusiness] Error al validar credenciales para: {} — {}", username, e.toString());
            throw new CampeonatoException(CampeonatoException.BUS_CREDENCIALES, e);

        } catch (Exception e) {
            if (conn != null) conn.error();
            log.error("[LoginBusiness] Error inesperado en validarCredenciales", e);
            throw new CampeonatoException(CampeonatoException.BUS_PROCESO, e);

        } finally {
            if (conn != null) conn.close();
        }
    }
}