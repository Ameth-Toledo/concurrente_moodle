package com.gestion.AlumnoGrupo.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gestion.AlumnoGrupo.application.DesinscribirAlumno_UseCase;
import com.gestion.AlumnoGrupo.application.GetAllInscripciones_UseCase;
import com.gestion.AlumnoGrupo.application.GetAlumnosByGrupo_UseCase;
import com.gestion.AlumnoGrupo.application.GetGruposByAlumno_UseCase;
import com.gestion.AlumnoGrupo.application.GetInscripcionStats_UseCase;
import com.gestion.AlumnoGrupo.application.InscribirAlumno_UseCase;
import com.gestion.AlumnoGrupo.infrastructure.adapters.MySQLAlumnoGrupoRepository;
import com.gestion.core.ConnMySQL;

@Configuration
public class AlumnoGrupoDependencies {
    
    // ============================================
    // REPOSITORY
    // ============================================
    @Bean
    public MySQLAlumnoGrupoRepository alumnoGrupoRepository() {
        return new MySQLAlumnoGrupoRepository(ConnMySQL.getInstance());
    }
    
    // ============================================
    // USE CASES
    // ============================================
    @Bean
    public InscribirAlumno_UseCase inscribirAlumnoUseCase(MySQLAlumnoGrupoRepository repository) {
        return new InscribirAlumno_UseCase(repository);
    }
    
    @Bean
    public DesinscribirAlumno_UseCase desinscribirAlumnoUseCase(MySQLAlumnoGrupoRepository repository) {
        return new DesinscribirAlumno_UseCase(repository);
    }
    
    @Bean
    public GetGruposByAlumno_UseCase getGruposByAlumnoUseCase(MySQLAlumnoGrupoRepository repository) {
        return new GetGruposByAlumno_UseCase(repository);
    }
    
    @Bean
    public GetAlumnosByGrupo_UseCase getAlumnosByGrupoUseCase(MySQLAlumnoGrupoRepository repository) {
        return new GetAlumnosByGrupo_UseCase(repository);
    }
    
    @Bean
    public GetAllInscripciones_UseCase getAllInscripcionesUseCase(MySQLAlumnoGrupoRepository repository) {
        return new GetAllInscripciones_UseCase(repository);
    }
    
    @Bean
    public GetInscripcionStats_UseCase getInscripcionStatsUseCase(MySQLAlumnoGrupoRepository repository) {
        return new GetInscripcionStats_UseCase(repository);
    }
}