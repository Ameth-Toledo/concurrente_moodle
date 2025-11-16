package com.gestion.AlumnoGrupo.infrastructure.adapters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gestion.AlumnoGrupo.domain.AlumnoGrupo_Repository;
import com.gestion.AlumnoGrupo.domain.entities.AlumnoGrupo;
import com.gestion.core.ConnMySQL;

public class MySQLAlumnoGrupoRepository implements AlumnoGrupo_Repository {
    private final ConnMySQL conn;
    private final ExecutorService executorService;

    public MySQLAlumnoGrupoRepository(ConnMySQL conn) {
        this.conn = conn;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public CompletableFuture<AlumnoGrupo> inscribir(AlumnoGrupo alumnoGrupo) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO alumno_grupo (alumno_id, grupo_id, fecha_inscripcion) " +
                          "VALUES (?, ?, ?)";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, alumnoGrupo.getAlumnoId());
                statement.setInt(2, alumnoGrupo.getGrupoId());
                statement.setTimestamp(3, Timestamp.valueOf(alumnoGrupo.getFechaInscripcion()));
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Error al inscribir alumno al grupo");
                }
                
                return alumnoGrupo;
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    throw new RuntimeException("El alumno ya está inscrito en este grupo");
                }
                throw new RuntimeException("Error al inscribir alumno: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> desinscribir(Integer alumnoId, Integer grupoId) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM alumno_grupo WHERE alumno_id = ? AND grupo_id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, alumnoId);
                statement.setInt(2, grupoId);
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Inscripción no encontrada");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al desinscribir alumno: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Integer>> getGruposByAlumno(Integer alumnoId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT grupo_id FROM alumno_grupo WHERE alumno_id = ?";
            
            List<Integer> grupoIds = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, alumnoId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        grupoIds.add(resultSet.getInt("grupo_id"));
                    }
                }
                
                return grupoIds;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener grupos del alumno: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Integer>> getAlumnosByGrupo(Integer grupoId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT alumno_id FROM alumno_grupo WHERE grupo_id = ?";
            
            List<Integer> alumnoIds = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, grupoId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        alumnoIds.add(resultSet.getInt("alumno_id"));
                    }
                }
                
                return alumnoIds;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener alumnos del grupo: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<AlumnoGrupo>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT alumno_id, grupo_id, fecha_inscripcion FROM alumno_grupo " +
                          "ORDER BY fecha_inscripcion DESC";
            
            List<AlumnoGrupo> inscripciones = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    inscripciones.add(mapResultSetToAlumnoGrupo(resultSet));
                }
                
                return inscripciones;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener inscripciones: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> existsInscripcion(Integer alumnoId, Integer grupoId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM alumno_grupo WHERE alumno_id = ? AND grupo_id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, alumnoId);
                statement.setInt(2, grupoId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al verificar inscripción: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Integer> countAlumnosInGrupo(Integer grupoId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM alumno_grupo WHERE grupo_id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, grupoId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al contar alumnos en grupo: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Integer> countGruposOfAlumno(Integer alumnoId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM alumno_grupo WHERE alumno_id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, alumnoId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al contar grupos del alumno: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> hasCapacity(Integer grupoId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT " +
                          "(SELECT COUNT(*) FROM alumno_grupo WHERE grupo_id = ?) as current_count, " +
                          "(SELECT capacidad_maxima FROM grupos WHERE id = ?) as max_capacity";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, grupoId);
                statement.setInt(2, grupoId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int currentCount = resultSet.getInt("current_count");
                        int maxCapacity = resultSet.getInt("max_capacity");
                        return currentCount < maxCapacity;
                    }
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al verificar capacidad del grupo: " + e.getMessage(), e);
            }
        }, executorService);
    }

    private AlumnoGrupo mapResultSetToAlumnoGrupo(ResultSet resultSet) throws SQLException {
        return new AlumnoGrupo(
            resultSet.getInt("alumno_id"),
            resultSet.getInt("grupo_id"),
            resultSet.getTimestamp("fecha_inscripcion").toLocalDateTime()
        );
    }

    public void shutdown() {
        executorService.shutdown();
    }
}