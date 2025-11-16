package com.gestion.Grupo.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gestion.Grupo.application.CreateGrupo_UseCase;
import com.gestion.Grupo.application.DeleteGrupo_UseCase;
import com.gestion.Grupo.application.GetAllGrupos_UseCase;
import com.gestion.Grupo.application.GetGrupoById_UseCase;
import com.gestion.Grupo.application.GetGruposByAsignatura_UseCase;
import com.gestion.Grupo.application.GetGruposByCuatrimestre_UseCase;
import com.gestion.Grupo.application.GetGruposByDocente_UseCase;
import com.gestion.Grupo.application.UpdateGrupo_UseCase;
import com.gestion.Grupo.infrastructure.adapters.MySQLGrupoRepository;
import com.gestion.core.ConnMySQL;

@Configuration
public class GrupoDependencies {
    
    // ============================================
    // REPOSITORY
    // ============================================
    @Bean
    public MySQLGrupoRepository grupoRepository() {
        return new MySQLGrupoRepository(ConnMySQL.getInstance());
    }
    
    // ============================================
    // USE CASES
    // ============================================
    @Bean
    public CreateGrupo_UseCase createGrupoUseCase(MySQLGrupoRepository repository) {
        return new CreateGrupo_UseCase(repository);
    }
    
    @Bean
    public GetAllGrupos_UseCase getAllGruposUseCase(MySQLGrupoRepository repository) {
        return new GetAllGrupos_UseCase(repository);
    }
    
    @Bean
    public GetGrupoById_UseCase getGrupoByIdUseCase(MySQLGrupoRepository repository) {
        return new GetGrupoById_UseCase(repository);
    }
    
    @Bean
    public GetGruposByCuatrimestre_UseCase getGruposByCuatrimestreUseCase(MySQLGrupoRepository repository) {
        return new GetGruposByCuatrimestre_UseCase(repository);
    }
    
    @Bean
    public GetGruposByDocente_UseCase getGruposByDocenteUseCase(MySQLGrupoRepository repository) {
        return new GetGruposByDocente_UseCase(repository);
    }
    
    @Bean
    public GetGruposByAsignatura_UseCase getGruposByAsignaturaUseCase(MySQLGrupoRepository repository) {
        return new GetGruposByAsignatura_UseCase(repository);
    }
    
    @Bean
    public UpdateGrupo_UseCase updateGrupoUseCase(MySQLGrupoRepository repository) {
        return new UpdateGrupo_UseCase(repository);
    }
    
    @Bean
    public DeleteGrupo_UseCase deleteGrupoUseCase(MySQLGrupoRepository repository) {
        return new DeleteGrupo_UseCase(repository);
    }
}