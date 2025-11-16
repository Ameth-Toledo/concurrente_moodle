package com.gestion.Alumno.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gestion.Alumno.application.CreateAlumno_UseCase;
import com.gestion.Alumno.application.DeleteAlumno_UseCase;
import com.gestion.Alumno.application.GetAllAlumnos_UseCase;
import com.gestion.Alumno.application.GetAlumnoById_UseCase;
import com.gestion.Alumno.application.GetAlumnoByMatricula_UseCase;
import com.gestion.Alumno.application.GetAlumnosByCuatrimestre_UseCase;
import com.gestion.Alumno.application.SearchAlumnos_UseCase;
import com.gestion.Alumno.application.UpdateAlumno_UseCase;
import com.gestion.Alumno.infrastructure.adapters.MySQLAlumnoRepository;
import com.gestion.core.ConnMySQL;

@Configuration
public class AlumnoDependencies {
    
    // ============================================
    // REPOSITORY
    // ============================================
    @Bean
    public MySQLAlumnoRepository alumnoRepository() {
        return new MySQLAlumnoRepository(ConnMySQL.getInstance());
    }
    
    // ============================================
    // USE CASES
    // ============================================
    @Bean
    public CreateAlumno_UseCase createAlumnoUseCase(MySQLAlumnoRepository repository) {
        return new CreateAlumno_UseCase(repository);
    }
    
    @Bean
    public GetAllAlumnos_UseCase getAllAlumnosUseCase(MySQLAlumnoRepository repository) {
        return new GetAllAlumnos_UseCase(repository);
    }
    
    @Bean
    public GetAlumnoById_UseCase getAlumnoByIdUseCase(MySQLAlumnoRepository repository) {
        return new GetAlumnoById_UseCase(repository);
    }
    
    @Bean
    public GetAlumnoByMatricula_UseCase getAlumnoByMatriculaUseCase(MySQLAlumnoRepository repository) {
        return new GetAlumnoByMatricula_UseCase(repository);
    }
    
    @Bean
    public GetAlumnosByCuatrimestre_UseCase getAlumnosByCuatrimestreUseCase(MySQLAlumnoRepository repository) {
        return new GetAlumnosByCuatrimestre_UseCase(repository);
    }
    
    @Bean
    public SearchAlumnos_UseCase searchAlumnosUseCase(MySQLAlumnoRepository repository) {
        return new SearchAlumnos_UseCase(repository);
    }
    
    @Bean
    public UpdateAlumno_UseCase updateAlumnoUseCase(MySQLAlumnoRepository repository) {
        return new UpdateAlumno_UseCase(repository);
    }
    
    @Bean
    public DeleteAlumno_UseCase deleteAlumnoUseCase(MySQLAlumnoRepository repository) {
        return new DeleteAlumno_UseCase(repository);
    }
}