package com.gestion.Alumno.infrastructure.adapters;

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

import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Alumno.domain.entities.Alumno;
import com.gestion.core.ConnMySQL;

public class MySQLAlumnoRepository implements Alumno_Repository {
    private final ConnMySQL conn;
    private final ExecutorService executorService;

    public MySQLAlumnoRepository(ConnMySQL conn) {
        this.conn = conn;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public CompletableFuture<Alumno> save(Alumno alumno) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO alumnos (nombre, apellido, matricula, cuatrimestre, email, programa_id, fecha_ingreso) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                
                statement.setString(1, alumno.getNombre());
                statement.setString(2, alumno.getApellido());
                statement.setString(3, alumno.getMatricula());
                statement.setInt(4, alumno.getCuatrimestre());
                statement.setString(5, alumno.getEmail());
                statement.setInt(6, alumno.getProgramaId());
                statement.setDate(7, Date.valueOf(alumno.getFechaIngreso()));
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Error al crear el alumno");
                }
                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        alumno.setId(generatedKeys.getInt(1));
                        return alumno;
                    } else {
                        throw new SQLException("Error al obtener el ID generado");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al guardar alumno: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Alumno>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, matricula, cuatrimestre, email, programa_id, fecha_ingreso " +
                          "FROM alumnos ORDER BY apellido, nombre";
            
            List<Alumno> alumnos = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    alumnos.add(mapResultSetToAlumno(resultSet));
                }
                
                return alumnos;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener alumnos: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Alumno> getById(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, matricula, cuatrimestre, email, programa_id, fecha_ingreso " +
                          "FROM alumnos WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToAlumno(resultSet);
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener alumno por ID: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Alumno> getByMatricula(String matricula) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, matricula, cuatrimestre, email, programa_id, fecha_ingreso " +
                          "FROM alumnos WHERE matricula = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, matricula);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToAlumno(resultSet);
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener alumno por matrícula: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Alumno>> getByCuatrimestre(Integer cuatrimestre) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, matricula, cuatrimestre, email, programa_id, fecha_ingreso " +
                          "FROM alumnos WHERE cuatrimestre = ? ORDER BY apellido, nombre";
            
            List<Alumno> alumnos = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, cuatrimestre);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        alumnos.add(mapResultSetToAlumno(resultSet));
                    }
                }
                
                return alumnos;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener alumnos por cuatrimestre: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Alumno>> getByPrograma(Integer programaId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, matricula, cuatrimestre, email, programa_id, fecha_ingreso " +
                          "FROM alumnos WHERE programa_id = ? ORDER BY cuatrimestre, apellido, nombre";
            
            List<Alumno> alumnos = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, programaId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        alumnos.add(mapResultSetToAlumno(resultSet));
                    }
                }
                
                return alumnos;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener alumnos por programa: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Alumno>> searchByName(String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, apellido, matricula, cuatrimestre, email, programa_id, fecha_ingreso " +
                          "FROM alumnos WHERE CONCAT(nombre, ' ', apellido) LIKE ? OR matricula LIKE ? " +
                          "ORDER BY apellido, nombre";
            
            List<Alumno> alumnos = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                String searchPattern = "%" + searchTerm + "%";
                statement.setString(1, searchPattern);
                statement.setString(2, searchPattern);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        alumnos.add(mapResultSetToAlumno(resultSet));
                    }
                }
                
                return alumnos;
            } catch (SQLException e) {
                throw new RuntimeException("Error al buscar alumnos: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> update(Alumno alumno) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE alumnos SET nombre = ?, apellido = ?, matricula = ?, " +
                          "cuatrimestre = ?, email = ?, programa_id = ? WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, alumno.getNombre());
                statement.setString(2, alumno.getApellido());
                statement.setString(3, alumno.getMatricula());
                statement.setInt(4, alumno.getCuatrimestre());
                statement.setString(5, alumno.getEmail());
                statement.setInt(6, alumno.getProgramaId());
                statement.setInt(7, alumno.getId());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Alumno no encontrado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar alumno: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> delete(Integer id) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM alumnos WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Alumno no encontrado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al eliminar alumno: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> existsByMatricula(String matricula) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM alumnos WHERE matricula = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, matricula);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al verificar matrícula: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> existsByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM alumnos WHERE email = ?";
            
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

    private Alumno mapResultSetToAlumno(ResultSet resultSet) throws SQLException {
        return new Alumno(
            resultSet.getInt("id"),
            resultSet.getString("nombre"),
            resultSet.getString("apellido"),
            resultSet.getString("matricula"),
            resultSet.getInt("cuatrimestre"),
            resultSet.getString("email"),
            resultSet.getInt("programa_id"),
            resultSet.getDate("fecha_ingreso").toLocalDate()
        );
    }

    public void shutdown() {
        executorService.shutdown();
    }
}