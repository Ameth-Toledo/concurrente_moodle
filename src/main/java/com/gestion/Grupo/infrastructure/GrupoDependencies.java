package com.gestion.Grupo.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gestion.Grupo.application.CreateGrupo_UseCase;
import com.gestion.Grupo.infrastructure.adapters.MySQLGrupoRepository;
import com.gestion.core.ConnMySQL;

@Configuration
public class GrupoDependencies {
    
    @Bean
    public MySQLGrupoRepository grupoRepository() {
        return new MySQLGrupoRepository(ConnMySQL.getInstance());
    }
    
    @Bean
    public CreateGrupo_UseCase createGrupoUseCase(MySQLGrupoRepository repository) {
        return new CreateGrupo_UseCase(repository);
    }
}