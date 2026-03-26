package com.campeonato.services;

import com.campeonato.inicio.beans.UsuarioSesion;

public interface ILoginService
{

    boolean login(String username, String password, UsuarioSesion usuarioSesion);

    void logout(String username, UsuarioSesion usuarioSesion);
}