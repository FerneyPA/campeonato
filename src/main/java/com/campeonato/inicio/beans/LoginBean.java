package com.campeonato.inicio.beans;

import com.campeonato.beans.BaseBean;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.services.ILoginService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

@Named
@RequestScoped
public class LoginBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;

	@Inject
	private ILoginService loginService;

	@Inject
	private UsuarioSesion usuarioSesion;

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String irHome() {
		return "home";
	}

	public String login() {

		try {
			boolean ok = loginService.login(username, password, usuarioSesion);

			if (ok) {
				mensajeInfo("login.exitoso", username);
				return "home";
			}

			return "login";

		} catch (CampeonatoException e) {
			mensajeError(e);
			return "login";

		} catch (Exception e) {
			mensajeError("error.inesperado");
			return "login";
		}
	}

	public String logout() {
	    try {
	        loginService.logout(usuarioSesion != null ? usuarioSesion.getUsername() : null, usuarioSesion);
	    } catch (Exception e) {
	        // ignorar
	    }
	    invalidarSesion();

	    FacesContext faces = FacesContext.getCurrentInstance();
	    String loginUrl = faces.getExternalContext().getRequestContextPath() + "/login.xhtml";

	    if (faces.getPartialViewContext().isAjaxRequest()) {
	        PrimeFaces.current().executeScript("window.top.location.href='" + loginUrl + "';");
	        return null;
	    }

	    return "login?faces-redirect=true";
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}