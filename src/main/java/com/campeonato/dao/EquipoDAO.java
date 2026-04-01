package com.campeonato.dao;

import com.campeonato.dao.registro.BaseDAO;
import com.campeonato.exception.CampeonatoException;
import com.campeonato.exception.LogManager;
import com.campeonato.modelo.Equipo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

public class EquipoDAO extends BaseDAO {

    private static final Logger log  = LogManager.getLogger(MenuDAO.class);

    public EquipoDAO(Connection conn) {
        super(conn);
    }

    // ─── LISTAR ───────────────────────────────────────────────────────────────

    public List<Equipo> listar() throws CampeonatoException {
        String metodo = "listar";

        String sql = """
                SELECT id_equipo, nombre, ciudad, fecha_fundacion
                FROM equipos
                ORDER BY nombre
                """;

        List<Equipo> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }

        } catch (SQLException e) {
            manejarError("DAO-001" + metodo, e);
        }

        return lista;
    }

    // ─── BUSCAR POR NOMBRE / CIUDAD ───────────────────────────────────────────

    public List<Equipo> buscar(String filtro) throws CampeonatoException {
        String metodo = "buscar";

        String sql = """
                SELECT id_equipo, nombre, ciudad, fecha_fundacion
                FROM equipos
                WHERE nombre LIKE ? OR ciudad LIKE ?
                ORDER BY nombre
                """;

        List<Equipo> lista = new ArrayList<>();
        String param = "%" + filtro + "%";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            ps.setString(2, param);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }

        } catch (SQLException e) {
            manejarError("DAO-001"+ metodo, e);
        }

        return lista;
    }

    // ─── OBTENER POR ID ───────────────────────────────────────────────────────

    public Equipo obtener(Integer idEquipo) throws CampeonatoException {
        String metodo = "obtener";

        String sql = """
                SELECT id_equipo, nombre, ciudad, fecha_fundacion
                FROM equipos
                WHERE id_equipo = ?
                """;

        Equipo equipo = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    equipo = mapear(rs);
                }
            }

        } catch (SQLException e) {
            manejarError("DAO-002"+ metodo, e);
        }

        return equipo;
    }

    // ─── INSERTAR ─────────────────────────────────────────────────────────────

    public void insertar(Equipo equipo) throws CampeonatoException {
        String metodo = "insertar";

        String sql = """
                INSERT INTO equipos (nombre, ciudad, fecha_fundacion)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipo.getNombre());
            ps.setString(2, equipo.getCiudad());
            ps.setObject(3, equipo.getFechaFundacion()); // LocalDate → DATE

            ps.executeUpdate();

        } catch (SQLException e) {
            manejarError("DAO-003"+ metodo, e);
        }

    }

    // ─── ACTUALIZAR ───────────────────────────────────────────────────────────

    public void actualizar(Equipo equipo) throws CampeonatoException {
        String metodo = "actualizar";

        String sql = """
                UPDATE equipos
                SET nombre = ?, ciudad = ?, fecha_fundacion = ?
                WHERE id_equipo = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, equipo.getNombre());
            ps.setString(2, equipo.getCiudad());
            ps.setObject(3, equipo.getFechaFundacion());
            ps.setInt(4, equipo.getIdEquipo());

            ps.executeUpdate();

        } catch (SQLException e) {
            manejarError("DAO-004"+ metodo, e);
        }

    }

    // ─── ELIMINAR ─────────────────────────────────────────────────────────────

    public void eliminar(Integer idEquipo) throws CampeonatoException {
        String metodo = "eliminar";

        String sql = "DELETE FROM equipos WHERE id_equipo = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.executeUpdate();

        } catch (SQLException e) {
            manejarError("DAO-005"+ metodo, e);
        }

    }

    // ─── MAPEO ────────────────────────────────────────────────────────────────

    private Equipo mapear(ResultSet rs) throws SQLException {
        LocalDate fecha = rs.getObject("fecha_fundacion", LocalDate.class);
        return new Equipo(
                rs.getInt("id_equipo"),
                rs.getString("nombre"),
                rs.getString("ciudad"),
                fecha
        );
    }
}