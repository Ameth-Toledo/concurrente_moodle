package com.gestion.Asignatura.infrastructure.adapters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;
import com.gestion.core.ConnMySQL;

public class MySQLAsignaturaRepository implements Asignatura_Repository {
    private final ConnMySQL conn;
    private final ExecutorService executorService;

    public MySQLAsignaturaRepository(ConnMySQL conn) {
        this.conn = conn;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public CompletableFuture<Asignatura> save(Asignatura asignatura) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO asignaturas (nombre, cuatrimestre, programa_id, creditos) " +
                          "VALUES (?, ?, ?, ?)";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                
                statement.setString(1, asignatura.getNombre());
                statement.setInt(2, asignatura.getCuatrimestre());
                statement.setInt(3, asignatura.getProgramaId());
                statement.setInt(4, asignatura.getCreditos());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Error al crear la asignatura");
                }
                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        asignatura.setId(generatedKeys.getInt(1));
                        return asignatura;
                    } else {
                        throw new SQLException("Error al obtener el ID generado");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al guardar asignatura: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Asignatura>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, cuatrimestre, programa_id, creditos " +
                          "FROM asignaturas ORDER BY cuatrimestre, nombre";
            
            List<Asignatura> asignaturas = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    asignaturas.add(mapResultSetToAsignatura(resultSet));
                }
                
                return asignaturas;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener asignaturas: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Asignatura> getById(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, cuatrimestre, programa_id, creditos " +
                          "FROM asignaturas WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToAsignatura(resultSet);
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener asignatura por ID: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Asignatura>> getByCuatrimestre(Integer cuatrimestre) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, cuatrimestre, programa_id, creditos " +
                          "FROM asignaturas WHERE cuatrimestre = ? ORDER BY nombre";
            
            List<Asignatura> asignaturas = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, cuatrimestre);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        asignaturas.add(mapResultSetToAsignatura(resultSet));
                    }
                }
                
                return asignaturas;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener asignaturas por cuatrimestre: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Asignatura>> getByPrograma(Integer programaId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, cuatrimestre, programa_id, creditos " +
                          "FROM asignaturas WHERE programa_id = ? ORDER BY cuatrimestre, nombre";
            
            List<Asignatura> asignaturas = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, programaId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        asignaturas.add(mapResultSetToAsignatura(resultSet));
                    }
                }
                
                return asignaturas;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener asignaturas por programa: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Asignatura>> searchByName(String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, cuatrimestre, programa_id, creditos " +
                          "FROM asignaturas WHERE nombre LIKE ? ORDER BY cuatrimestre, nombre";
            
            List<Asignatura> asignaturas = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                String searchPattern = "%" + searchTerm + "%";
                statement.setString(1, searchPattern);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        asignaturas.add(mapResultSetToAsignatura(resultSet));
                    }
                }
                
                return asignaturas;
            } catch (SQLException e) {
                throw new RuntimeException("Error al buscar asignaturas: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> update(Asignatura asignatura) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE asignaturas SET nombre = ?, cuatrimestre = ?, " +
                          "programa_id = ?, creditos = ? WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, asignatura.getNombre());
                statement.setInt(2, asignatura.getCuatrimestre());
                statement.setInt(3, asignatura.getProgramaId());
                statement.setInt(4, asignatura.getCreditos());
                statement.setInt(5, asignatura.getId());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Asignatura no encontrada");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar asignatura: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> delete(Integer id) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM asignaturas WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Asignatura no encontrada");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al eliminar asignatura: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> existsByNombre(String nombre, Integer programaId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM asignaturas WHERE nombre = ? AND programa_id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, nombre);
                statement.setInt(2, programaId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al verificar existencia de asignatura: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Integer> countByPrograma(Integer programaId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM asignaturas WHERE programa_id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, programaId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al contar asignaturas por programa: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Integer> countByCuatrimestre(Integer cuatrimestre) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM asignaturas WHERE cuatrimestre = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, cuatrimestre);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al contar asignaturas por cuatrimestre: " + e.getMessage(), e);
            }
        }, executorService);
    }

    private Asignatura mapResultSetToAsignatura(ResultSet resultSet) throws SQLException {
        return new Asignatura(
            resultSet.getInt("id"),
            resultSet.getString("nombre"),
            resultSet.getInt("cuatrimestre"),
            resultSet.getInt("programa_id"),
            resultSet.getInt("creditos")
        );
    }

    public void shutdown() {
        executorService.shutdown();
    }
}