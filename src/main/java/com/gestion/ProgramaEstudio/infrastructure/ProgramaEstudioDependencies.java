package com.gestion.ProgramaEstudio.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gestion.ProgramaEstudio.application.CreateProgramaEstudio_UseCase;
import com.gestion.ProgramaEstudio.application.DeleteProgramaEstudio_UseCase;
import com.gestion.ProgramaEstudio.application.GetAllProgramasEstudio_UseCase;
import com.gestion.ProgramaEstudio.application.GetProgramaEstudioById_UseCase;
import com.gestion.ProgramaEstudio.application.GetProgramaEstudioStats_UseCase;
import com.gestion.ProgramaEstudio.application.SearchProgramasEstudio_UseCase;
import com.gestion.ProgramaEstudio.application.UpdateProgramaEstudio_UseCase;
import com.gestion.ProgramaEstudio.infrastructure.adapters.MySQLProgramaEstudioRepository;
import com.gestion.core.ConnMySQL;

@Configuration
public class ProgramaEstudioDependencies {
    
    // ============================================
    // REPOSITORY
    // ============================================
    @Bean
    public MySQLProgramaEstudioRepository programaEstudioRepository() {
        return new MySQLProgramaEstudioRepository(ConnMySQL.getInstance());
    }
    
    // ============================================
    // USE CASES
    // ============================================
    @Bean
    public CreateProgramaEstudio_UseCase createProgramaEstudioUseCase(MySQLProgramaEstudioRepository repository) {
        return new CreateProgramaEstudio_UseCase(repository);
    }
    
    @Bean
    public GetAllProgramasEstudio_UseCase getAllProgramasEstudioUseCase(MySQLProgramaEstudioRepository repository) {
        return new GetAllProgramasEstudio_UseCase(repository);
    }
    
    @Bean
    public GetProgramaEstudioById_UseCase getProgramaEstudioByIdUseCase(MySQLProgramaEstudioRepository repository) {
        return new GetProgramaEstudioById_UseCase(repository);
    }
    
    @Bean
    public SearchProgramasEstudio_UseCase searchProgramasEstudioUseCase(MySQLProgramaEstudioRepository repository) {
        return new SearchProgramasEstudio_UseCase(repository);
    }
    
    @Bean
    public UpdateProgramaEstudio_UseCase updateProgramaEstudioUseCase(MySQLProgramaEstudioRepository repository) {
        return new UpdateProgramaEstudio_UseCase(repository);
    }
    
    @Bean
    public DeleteProgramaEstudio_UseCase deleteProgramaEstudioUseCase(MySQLProgramaEstudioRepository repository) {
        return new DeleteProgramaEstudio_UseCase(repository);
    }
    
    @Bean
    public GetProgramaEstudioStats_UseCase getProgramaEstudioStatsUseCase(MySQLProgramaEstudioRepository repository) {
        return new GetProgramaEstudioStats_UseCase(repository);
    }
}