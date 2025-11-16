package com.gestion.Docente.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gestion.Docente.application.AssignAsignatura_UseCase;
import com.gestion.Docente.application.CreateDocente_UseCase;
import com.gestion.Docente.application.DeleteDocente_UseCase;
import com.gestion.Docente.application.GetAllDocentes_UseCase;
import com.gestion.Docente.application.GetAsignaturasByDocente_UseCase;
import com.gestion.Docente.application.GetDocenteById_UseCase;
import com.gestion.Docente.application.RemoveAsignatura_UseCase;
import com.gestion.Docente.application.SearchDocentes_UseCase;
import com.gestion.Docente.application.UpdateDocente_UseCase;
import com.gestion.Docente.infrastructure.adapters.MySQLDocenteRepository;
import com.gestion.core.ConnMySQL;

@Configuration
public class DocenteDependencies {
    
    // ============================================
    // REPOSITORY
    // ============================================
    @Bean
    public MySQLDocenteRepository docenteRepository() {
        return new MySQLDocenteRepository(ConnMySQL.getInstance());
    }
    
    // ============================================
    // USE CASES - CRUD
    // ============================================
    @Bean
    public CreateDocente_UseCase createDocenteUseCase(MySQLDocenteRepository repository) {
        return new CreateDocente_UseCase(repository);
    }
    
    @Bean
    public GetAllDocentes_UseCase getAllDocentesUseCase(MySQLDocenteRepository repository) {
        return new GetAllDocentes_UseCase(repository);
    }
    
    @Bean
    public GetDocenteById_UseCase getDocenteByIdUseCase(MySQLDocenteRepository repository) {
        return new GetDocenteById_UseCase(repository);
    }
    
    @Bean
    public SearchDocentes_UseCase searchDocentesUseCase(MySQLDocenteRepository repository) {
        return new SearchDocentes_UseCase(repository);
    }
    
    @Bean
    public UpdateDocente_UseCase updateDocenteUseCase(MySQLDocenteRepository repository) {
        return new UpdateDocente_UseCase(repository);
    }
    
    @Bean
    public DeleteDocente_UseCase deleteDocenteUseCase(MySQLDocenteRepository repository) {
        return new DeleteDocente_UseCase(repository);
    }
    
    // ============================================
    // USE CASES - ASIGNATURAS
    // ============================================
    @Bean
    public AssignAsignatura_UseCase assignAsignaturaUseCase(MySQLDocenteRepository repository) {
        return new AssignAsignatura_UseCase(repository);
    }
    
    @Bean
    public RemoveAsignatura_UseCase removeAsignaturaUseCase(MySQLDocenteRepository repository) {
        return new RemoveAsignatura_UseCase(repository);
    }
    
    @Bean
    public GetAsignaturasByDocente_UseCase getAsignaturasByDocenteUseCase(MySQLDocenteRepository repository) {
        return new GetAsignaturasByDocente_UseCase(repository);
    }
}