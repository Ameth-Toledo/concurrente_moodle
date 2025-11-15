package src.main.java.com.gestion.Grupo.infrastructure.adapters;

import src.main.java.com.gestion.Grupo.domain.Grupo_Repository;
import src.main.java.com.gestion.Grupo.domain.entities.Grupo;
import src.main.java.com.gestion.core.ConnMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQLGrupoRepository implements Grupo_Repository {
    private final ConnMySQL conn;
    private final ExecutorService executorService;

    public MySQLGrupoRepository(ConnMySQL conn) {
        this.conn = conn;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public CompletableFuture<Grupo> save(Grupo grupo) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO grupos (nombre, asignatura_id, docente_id, cuatrimestre, capacidad_maxima) " +
                          "VALUES (?, ?, ?, ?, ?)";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                
                statement.setString(1, grupo.getNombre());
                statement.setInt(2, grupo.getAsignaturaId());
                statement.setInt(3, grupo.getDocenteId());
                statement.setInt(4, grupo.getCuatrimestre());
                statement.setInt(5, grupo.getCapacidadMaxima());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Error al crear el grupo");
                }
                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        grupo.setId(generatedKeys.getInt(1));
                        return grupo;
                    } else {
                        throw new SQLException("Error al obtener el ID generado");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al guardar grupo: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Grupo>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, asignatura_id, docente_id, cuatrimestre, capacidad_maxima " +
                          "FROM grupos ORDER BY cuatrimestre, nombre";
            
            List<Grupo> grupos = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                
                while (resultSet.next()) {
                    grupos.add(mapResultSetToGrupo(resultSet));
                }
                
                return grupos;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener grupos: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Grupo> getById(Integer id) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, asignatura_id, docente_id, cuatrimestre, capacidad_maxima " +
                          "FROM grupos WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToGrupo(resultSet);
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener grupo por ID: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Grupo>> getByCuatrimestre(Integer cuatrimestre) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, asignatura_id, docente_id, cuatrimestre, capacidad_maxima " +
                          "FROM grupos WHERE cuatrimestre = ? ORDER BY nombre";
            
            List<Grupo> grupos = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, cuatrimestre);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        grupos.add(mapResultSetToGrupo(resultSet));
                    }
                }
                
                return grupos;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener grupos por cuatrimestre: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Grupo>> getByDocente(Integer docenteId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, asignatura_id, docente_id, cuatrimestre, capacidad_maxima " +
                          "FROM grupos WHERE docente_id = ? ORDER BY cuatrimestre, nombre";
            
            List<Grupo> grupos = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, docenteId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        grupos.add(mapResultSetToGrupo(resultSet));
                    }
                }
                
                return grupos;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener grupos por docente: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Grupo>> getByAsignatura(Integer asignaturaId) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT id, nombre, asignatura_id, docente_id, cuatrimestre, capacidad_maxima " +
                          "FROM grupos WHERE asignatura_id = ? ORDER BY nombre";
            
            List<Grupo> grupos = new ArrayList<>();
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, asignaturaId);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        grupos.add(mapResultSetToGrupo(resultSet));
                    }
                }
                
                return grupos;
            } catch (SQLException e) {
                throw new RuntimeException("Error al obtener grupos por asignatura: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> update(Grupo grupo) {
        return CompletableFuture.runAsync(() -> {
            String query = "UPDATE grupos SET nombre = ?, asignatura_id = ?, docente_id = ?, " +
                          "cuatrimestre = ?, capacidad_maxima = ? WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setString(1, grupo.getNombre());
                statement.setInt(2, grupo.getAsignaturaId());
                statement.setInt(3, grupo.getDocenteId());
                statement.setInt(4, grupo.getCuatrimestre());
                statement.setInt(5, grupo.getCapacidadMaxima());
                statement.setInt(6, grupo.getId());
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Grupo no encontrado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar grupo: " + e.getMessage(), e);
            }
        }, executorService);
    }

    @Override
    public CompletableFuture<Void> delete(Integer id) {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM grupos WHERE id = ?";
            
            try (Connection connection = conn.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                
                statement.setInt(1, id);
                
                int affectedRows = statement.executeUpdate();
                
                if (affectedRows == 0) {
                    throw new SQLException("Grupo no encontrado");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al eliminar grupo: " + e.getMessage(), e);
            }
        }, executorService);
    }

    private Grupo mapResultSetToGrupo(ResultSet resultSet) throws SQLException {
        return new Grupo(
            resultSet.getInt("id"),
            resultSet.getString("nombre"),
            resultSet.getInt("asignatura_id"),
            resultSet.getInt("docente_id"),
            resultSet.getInt("cuatrimestre"),
            resultSet.getInt("capacidad_maxima")
        );
    }

    public void shutdown() {
        executorService.shutdown();
    }
}