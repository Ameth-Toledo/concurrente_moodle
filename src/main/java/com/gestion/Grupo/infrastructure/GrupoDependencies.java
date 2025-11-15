package src.main.java.com.gestion.Grupo.infrastructure;

import src.main.java.com.gestion.Grupo.application.CreateGrupo_UseCase;
import src.main.java.com.gestion.Grupo.infrastructure.adapters.MySQLGrupoRepository;
import src.main.java.com.gestion.core.ConnMySQL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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