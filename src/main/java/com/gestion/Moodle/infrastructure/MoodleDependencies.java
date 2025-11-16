package com.gestion.Moodle.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gestion.Alumno.infrastructure.adapters.MySQLAlumnoRepository;
import com.gestion.Asignatura.infrastructure.adapters.MySQLAsignaturaRepository;
import com.gestion.Docente.infrastructure.adapters.MySQLDocenteRepository;
import com.gestion.Moodle.application.MoodleSyncService;
import com.gestion.Moodle.infrastructure.client.MoodleClient;

@Configuration
public class MoodleDependencies {
    
    @Bean
    public MoodleClient moodleClient() {
        return new MoodleClient();
    }
    
    @Bean
    public MoodleSyncService moodleSyncService(
            MoodleClient moodleClient,
            MySQLAlumnoRepository alumnoRepository,
            MySQLDocenteRepository docenteRepository,
            MySQLAsignaturaRepository asignaturaRepository) {
        return new MoodleSyncService(
            moodleClient,
            alumnoRepository,
            docenteRepository,
            asignaturaRepository
        );
    }
}