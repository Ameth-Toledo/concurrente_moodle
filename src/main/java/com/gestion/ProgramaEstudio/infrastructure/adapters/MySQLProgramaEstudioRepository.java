package com.gestion.ProgramaEstudio.infrastructure.adapters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gestion.ProgramaEstudio.domain.ProgramaEstudio_Repository;
import com.gestion.ProgramaEstudio.domain.entities.ProgramaEstudio;
import com.gestion.core.ConnMySQL;

public class MySQLProgramaEstudioRepository implements ProgramaEstudio_Repository {
    private final ConnMySQL conn;
    private final ExecutorService executorService;

    public MySQLProgramaEstudioRepository(ConnMySQL conn) {
        this.conn = conn;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public CompletableFuture<ProgramaEstudio> save(ProgramaEstudio programaEstudio) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO programa_estudio (nombre, num_cuatrimestres, fecha_creacion) " +
                          "VALUES (?, ?, ?)";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                
                statement.setString(1, programaEstudio.getNombre());
                statement.setInt(2, programaEstudio.getNumCuatrimestres());
                statement.setTimestamp(3, Timestamp.valueOf(programaEstudio.getFechaCreacion()));
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Error al crear el programa de estudio");
                }
                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        programaEstudio.setId(generatedKeys.getInt(1));
                        return programaEstudio;
                    } else {
                        throw new SQLException("Error al obtener el ID generado");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al guardar programa de estudio: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<ProgramaEstudio>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, num_cuatrimestres, fecha_creacion " +
                          "FROM programa_estudio ORDER BY nombre";
            
            List<ProgramaEstudio> programas = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    programas.add(mapResultSetToProgramaEstudio(resultSet));
                }
                
                return programas;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener programas de estudio: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<ProgramaEstudio> getById(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, num_cuatrimestres, fecha_creacion " +
                          "FROM programa_estudio WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToProgramaEstudio(resultSet);
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener programa de estudio por ID: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<ProgramaEstudio> getByNombre(String nombre) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, num_cuatrimestres, fecha_creacion " +
                          "FROM programa_estudio WHERE nombre = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, nombre);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToProgramaEstudio(resultSet);
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener programa de estudio por nombre: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<ProgramaEstudio>> searchByName(String searchTerm) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, num_cuatrimestres, fecha_creacion " +
                          "FROM programa_estudio WHERE nombre LIKE ? ORDER BY nombre";
            
            List<ProgramaEstudio> programas = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                String searchPattern = "%" + searchTerm + "%";
                statement.setString(1, searchPattern);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        programas.add(mapResultSetToProgramaEstudio(resultSet));
                    }
                }
                
                return programas;
            } catch (SQLException e) {
                throw new RuntimeException("Error al buscar programas de estudio: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> update(ProgramaEstudio programaEstudio) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE programa_estudio SET nombre = ?, num_cuatrimestres = ? WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, programaEstudio.getNombre());
                statement.setInt(2, programaEstudio.getNumCuatrimestres());
                statement.setInt(3, programaEstudio.getId());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Programa de estudio no encontrado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar programa de estudio: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> delete(Integer id) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM programa_estudio WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Programa de estudio no encontrado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al eliminar programa de estudio: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> existsByNombre(String nombre) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM programa_estudio WHERE nombre = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, nombre);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al verificar existencia de programa: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Integer> countAlumnos(Integer programaId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT COUNT(*) FROM alumnos WHERE programa_id = ?";
            
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
                throw new RuntimeException("Error al contar alumnos del programa: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Integer> countAsignaturas(Integer programaId) {
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
                throw new RuntimeException("Error al contar asignaturas del programa: " + e.getMessage(), e);
            }
        }, executorService);
    }

    private ProgramaEstudio mapResultSetToProgramaEstudio(ResultSet resultSet) throws SQLException {
        return new ProgramaEstudio(
            resultSet.getInt("id"),
            resultSet.getString("nombre"),
            resultSet.getInt("num_cuatrimestres"),
            resultSet.getTimestamp("fecha_creacion").toLocalDateTime()
        );
    }

    public void shutdown() {
        executorService.shutdown();
    }
}