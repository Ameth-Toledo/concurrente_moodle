package src.main.java.com.gestion.Grupo.domain;


import src.main.java.com.gestion.Grupo.domain.entities.Grupo;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Grupo_Repository {
    
    CompletableFuture<Grupo> save(Grupo grupo);
    
    CompletableFuture<List<Grupo>> getAll();
    
    CompletableFuture<Grupo> getById(Integer id);
    
    CompletableFuture<List<Grupo>> getByCuatrimestre(Integer cuatrimestre);
    
    CompletableFuture<List<Grupo>> getByDocente(Integer docenteId);
    
    CompletableFuture<List<Grupo>> getByAsignatura(Integer asignaturaId);
    
    CompletableFuture<Void> update(Grupo grupo);
    
    CompletableFuture<Void> delete(Integer id);
}