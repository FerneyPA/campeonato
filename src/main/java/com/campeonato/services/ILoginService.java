package com.campeonato.services;

import com.campeonato.beans.UsuarioSesion;

public interface ILoginService {

    // ============================================================
    // Punto de entrada al proceso de login
    // El Service loguea la operación y delega al Business
    // ============================================================
    boolean login(String username, String password, UsuarioSesion usuarioSesion);

    // ============================================================
    // Punto de entrada al proceso de logout
    // El Service loguea la operación
    // ============================================================
    void logout(String username, UsuarioSesion usuarioSesion);
}