package com.campeonato.inicio.beans;

import com.campeonato.beans.BaseBean;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.services.ILoginService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

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
	    loginService.logout(usuarioSesion.getUsername(), usuarioSesion);
	    invalidarSesion();
	    return "login";
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}