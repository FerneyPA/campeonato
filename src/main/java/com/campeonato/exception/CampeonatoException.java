package com.campeonato.exception;

public class CampeonatoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String codigo;

    // ============================================================
    // Códigos DAO — errores en la capa de acceso a datos
    // ============================================================
    public static final String DAO_CONSULTA      = "DAO-001";
    public static final String DAO_INSERCION     = "DAO-002";
    public static final String DAO_ACTUALIZACION = "DAO-003";
    public static final String DAO_ELIMINACION   = "DAO-004";
    public static final String DAO_CONEXION      = "DAO-005";

    // ============================================================
    // Códigos Business — errores en la capa de negocio
    // ============================================================
    public static final String BUS_VALIDACION    = "BUS-001";
    public static final String BUS_CREDENCIALES  = "BUS-002";
    public static final String BUS_SESION        = "BUS-003";
    public static final String BUS_PROCESO       = "BUS-004";
    public static final String BUS_NO_ENCONTRADO = "BUS-005";

    // ============================================================
    // Constructor con código — busca el mensaje en
    // mensajes.properties automáticamente
    //
    // Uso:
    //   throw new CampeonatoException(CampeonatoException.BUS_CREDENCIALES);
    // ============================================================
    public CampeonatoException(String codigo) {
        super(MensajesManager.getMensaje(codigo));
        this.codigo = codigo;
    }

    // ============================================================
    // Constructor con código + causa
    //
    // Uso:
    //   throw new CampeonatoException(CampeonatoException.DAO_CONSULTA, e);
    // ============================================================
    public CampeonatoException(String codigo, Throwable causa) {
        super(MensajesManager.getMensaje(codigo), causa);
        this.codigo = codigo;
    }

    // ============================================================
    // Constructor solo mensaje — para casos específicos sin código
    // El código queda como "SIN-CODIGO" en el log
    //
    // Uso:
    //   throw new CampeonatoException("El torneo ya tiene 16 equipos registrados.");
    // ============================================================
    public CampeonatoException(boolean esMensajeDirecto, String mensaje) {
        super(mensaje);
        this.codigo = "SIN-CODIGO";
    }

    // ============================================================
    // Constructor solo mensaje + causa
    //
    // Uso:
    //   throw new CampeonatoException("El torneo ya tiene 16 equipos.", e);
    // ============================================================
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