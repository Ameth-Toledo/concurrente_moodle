package com.gestion.Asignatura.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gestion.Asignatura.application.CreateAsignatura_UseCase;
import com.gestion.Asignatura.application.DeleteAsignatura_UseCase;
import com.gestion.Asignatura.application.GetAllAsignaturas_UseCase;
import com.gestion.Asignatura.application.GetAsignaturaById_UseCase;
import com.gestion.Asignatura.application.GetAsignaturasByCuatrimestre_UseCase;
import com.gestion.Asignatura.application.GetAsignaturasByPrograma_UseCase;
import com.gestion.Asignatura.application.SearchAsignaturas_UseCase;
import com.gestion.Asignatura.application.UpdateAsignatura_UseCase;
import com.gestion.Asignatura.infrastructure.adapters.MySQLAsignaturaRepository;
import com.gestion.core.ConnMySQL;

@Configuration
public class AsignaturaDependencies {
    
    // ============================================
    // REPOSITORY
    // ============================================
    @Bean
    public MySQLAsignaturaRepository asignaturaRepository() {
        return new MySQLAsignaturaRepository(ConnMySQL.getInstance());
    }
    
    // ============================================
    // USE CASES
    // ============================================
    @Bean
    public CreateAsignatura_UseCase createAsignaturaUseCase(MySQLAsignaturaRepository repository) {
        return new CreateAsignatura_UseCase(repository);
    }
    
    @Bean
    public GetAllAsignaturas_UseCase getAllAsignaturasUseCase(MySQLAsignaturaRepository repository) {
        return new GetAllAsignaturas_UseCase(repository);
    }
    
    @Bean
    public GetAsignaturaById_UseCase getAsignaturaByIdUseCase(MySQLAsignaturaRepository repository) {
        return new GetAsignaturaById_UseCase(repository);
    }
    
    @Bean
    public GetAsignaturasByCuatrimestre_UseCase getAsignaturasByCuatrimestreUseCase(MySQLAsignaturaRepository repository) {
        return new GetAsignaturasByCuatrimestre_UseCase(repository);
    }
    
    @Bean
    public GetAsignaturasByPrograma_UseCase getAsignaturasByProgramaUseCase(MySQLAsignaturaRepository repository) {
        return new GetAsignaturasByPrograma_UseCase(repository);
    }
    
    @Bean
    public SearchAsignaturas_UseCase searchAsignaturasUseCase(MySQLAsignaturaRepository repository) {
        return new SearchAsignaturas_UseCase(repository);
    }
    
    @Bean
    public UpdateAsignatura_UseCase updateAsignaturaUseCase(MySQLAsignaturaRepository repository) {
        return new UpdateAsignatura_UseCase(repository);
    }
    
    @Bean
    public DeleteAsignatura_UseCase deleteAsignaturaUseCase(MySQLAsignaturaRepository repository) {
        return new DeleteAsignatura_UseCase(repository);
    }
}