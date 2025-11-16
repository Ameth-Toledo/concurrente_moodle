package com.gestion.Docente.infrastructure.adapters;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gestion.Docente.domain.Docente_Repository;
import com.gestion.Docente.domain.entities.Docente;
import com.gestion.core.ConnMySQL;

public class MySQLDocenteRepository implements Docente_Repository {
    private final ConnMySQL conn;
    private final ExecutorService executorService;

    public MySQLDocenteRepository(ConnMySQL conn) {
        this.conn = conn;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public CompletableFuture<Docente> save(Docente docente) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO docentes (nombre, apellido, email, telefono, fecha_contratacion) " +
                          "VALUES (?, ?, ?, ?, ?)";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                
                statement.setString(1, docente.getNombre());
                statement.setString(2, docente.getApellido());
                statement.setString(3, docente.getEmail());
                statement.setString(4, docente.getTelefono());
                statement.setDate(5, Date.valueOf(docente.getFechaContratacion()));
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Error al crear el docente");
                }
                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        docente.setId(generatedKeys.getInt(1));
                        return docente;
                    } else {
                        throw new SQLException("Error al obtener el ID generado");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al guardar docente: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Docente>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, email, telefono, fecha_contratacion " +
                          "FROM docentes ORDER BY apellido, nombre";
            
            List<Docente> docentes = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    docentes.add(mapResultSetToDocente(resultSet));
                }
                
                return docentes;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener docentes: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Docente> getById(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, email, telefono, fecha_contratacion " +
                          "FROM docentes WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToDocente(resultSet);
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener docente por ID: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Docente> getByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, email, telefono, fecha_contratacion " +
                          "FROM docentes WHERE email = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, email);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToDocente(resultSet);
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener docente por email: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Docente>> searchByName(String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, email, telefono, fecha_contratacion " +
                          "FROM docentes WHERE CONCAT(nombre, ' ', apellido) LIKE ? OR email LIKE ? " +
                          "ORDER BY apellido, nombre";
            
            List<Docente> docentes = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                String searchPattern = "%" + searchTerm + "%";
                statement.setString(1, searchPattern);
                statement.setString(2, searchPattern);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        docentes.add(mapResultSetToDocente(resultSet));
                    }
                }
                
                return docentes;
            } catch (SQLException e) {
                throw new RuntimeException("Error al buscar docentes: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> update(Docente docente) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE docentes SET nombre = ?, apellido = ?, email = ?, telefono = ? WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, docente.getNombre());
                statement.setString(2, docente.getApellido());
                statement.setString(3, docente.getEmail());
                statement.setString(4, docente.getTelefono());
                statement.setInt(5, docente.getId());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Docente no encontrado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar docente: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> delete(Integer id) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM docentes WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Docente no encontrado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al eliminar docente: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> existsByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM docentes WHERE email = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, email);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al verificar email: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> assignAsignatura(Integer docenteId, Integer asignaturaId) {
        return CompletableFuture.runAsync(() -> {
            String query = "INSERT INTO docente_asignatura (docente_id, asignatura_id) VALUES (?, ?) " +
                          "ON DUPLICATE KEY UPDATE docente_id = docente_id";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, docenteId);
                statement.setInt(2, asignaturaId);
                
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al asignar asignatura: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> removeAsignatura(Integer docenteId, Integer asignaturaId) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM docente_asignatura WHERE docente_id = ? AND asignatura_id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, docenteId);
                statement.setInt(2, asignaturaId);
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Relación no encontrada");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al remover asignatura: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Integer>> getAsignaturasByDocente(Integer docenteId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT asignatura_id FROM docente_asignatura WHERE docente_id = ?";
            
            List<Integer> asignaturaIds = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, docenteId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        asignaturaIds.add(resultSet.getInt("asignatura_id"));
                    }
                }
                
                return asignaturaIds;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener asignaturas del docente: " + e.getMessage(), e);
            }
        }, executorService);
    }

    private Docente mapResultSetToDocente(ResultSet resultSet) throws SQLException {
        return new Docente(
            resultSet.getInt("id"),
            resultSet.getString("nombre"),
            resultSet.getString("apellido"),
            resultSet.getString("email"),
            resultSet.getString("telefono"),
            resultSet.getDate("fecha_contratacion").toLocalDate()
        );
    }

    public void shutdown() {
        executorService.shutdown();
    }
}