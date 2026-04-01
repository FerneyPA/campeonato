package com.campeonato.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MensajesManagerTest {

    @Test
    public void testMensajesUtf8() {
        String guardado = MensajesManager.getMensaje("info.guardado");
        assertEquals("Registro guardado correctamente.", guardado);

        String eliminado = MensajesManager.getMensaje("info.eliminado");
        assertEquals("Registro eliminado correctamente.", eliminado);

        String daoError = MensajesManager.getError("DAO-005");
        assertEquals("Error de conexión con la base de datos.", daoError);
    }
}
