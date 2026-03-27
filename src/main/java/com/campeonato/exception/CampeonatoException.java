package com.campeonato.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CampeonatoException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String codigo;

    // ============================================================
    // Códigos DAO
    // ============================================================
    public static final String DAO_CONSULTA      = "DAO-001";
    public static final String DAO_INSERCION     = "DAO-002";
    public static final String DAO_ACTUALIZACION = "DAO-003";
    public static final String DAO_ELIMINACION   = "DAO-004";
    public static final String DAO_CONEXION      = "DAO-005";

    // ============================================================
    // Códigos Business
    // ============================================================
    public static final String BUS_VALIDACION    = "BUS-001";
    public static final String BUS_CREDENCIALES  = "BUS-002";
    public static final String BUS_SESION        = "BUS-003";
    public static final String BUS_PROCESO       = "BUS-004";
    public static final String BUS_NO_ENCONTRADO = "BUS-005";

    public CampeonatoException(String codigo) {
        super(MensajesManager.getError(codigo));
        this.codigo = codigo;
    }
    public CampeonatoException(String codigo, Throwable causa) {
        super(MensajesManager.getError(codigo), causa);
        this.codigo = codigo;
    }
    public CampeonatoException(boolean esMensajeDirecto, String mensaje) {
        super(mensaje);
        this.codigo = "SIN-CODIGO";
    }
    public CampeonatoException(boolean esMensajeDirecto, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigo = "SIN-CODIGO";
    }

    public String getCodigo() {
        return codigo;
    }
    @Override
    public String toString() {
        return "[" + codigo + "] " + getMessage();
    }
}