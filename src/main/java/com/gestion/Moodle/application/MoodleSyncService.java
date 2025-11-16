package com.gestion.Moodle.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.gestion.Alumno.domain.Alumno_Repository;
import com.gestion.Asignatura.domain.Asignatura_Repository;
import com.gestion.Asignatura.domain.entities.Asignatura;
import com.gestion.Docente.domain.Docente_Repository;
import com.gestion.Moodle.infrastructure.client.MoodleClient;

public class MoodleSyncService {
    private final MoodleClient moodleClient;
    private final Alumno_Repository alumnoRepository;
    private final Docente_Repository docenteRepository;
    private final Asignatura_Repository asignaturaRepository;

    // IDs de roles en Moodle (pueden variar según instalación)
    private static final int STUDENT_ROLE_ID = 5;
    private static final int TEACHER_ROLE_ID = 3;
    private static final int DEFAULT_CATEGORY_ID = 1;

    public MoodleSyncService(MoodleClient moodleClient,
                            Alumno_Repository alumnoRepository,
                            Docente_Repository docenteRepository,
                            Asignatura_Repository asignaturaRepository) {
        this.moodleClient = moodleClient;
        this.alumnoRepository = alumnoRepository;
        this.docenteRepository = docenteRepository;
        this.asignaturaRepository = asignaturaRepository;
    }

    /**
     * Sincroniza un alumno con Moodle
     */
    public CompletableFuture<Map<String, Object>> syncAlumno(Integer alumnoId) {
        return alumnoRepository.getById(alumnoId)
            .thenCompose(alumno -> {
                if (alumno == null) {
                    throw new RuntimeException("Alumno no encontrado con ID: " + alumnoId);
                }

                // Verificar si el usuario ya existe en Moodle
                return moodleClient.getUsersByField("email", alumno.getEmail())
                    .thenCompose(existingUsers -> {
                        if (existingUsers.isArray() && existingUsers.size() > 0) {
                            // Usuario ya existe
                            JsonNode user = existingUsers.get(0);
                            Map<String, Object> result = new HashMap<>();
                            result.put("action", "existing");
                            result.put("moodleUserId", user.get("id").asInt());
                            result.put("username", user.get("username").asText());
                            result.put("message", "El usuario ya existe en Moodle");
                            return CompletableFuture.completedFuture(result);
                        }

                        // Crear usuario en Moodle
                        String username = alumno.getMatricula().toLowerCase();
                        String password = generatePassword(alumno.getMatricula());

                        return moodleClient.createUser(
                            username,
                            password,
                            alumno.getNombre(),
                            alumno.getApellido(),
                            alumno.getEmail()
                        ).thenApply(response -> {
                            if (response.isArray() && response.size() > 0) {
                                JsonNode user = response.get(0);
                                Map<String, Object> result = new HashMap<>();
                                result.put("action", "created");
                                result.put("moodleUserId", user.get("id").asInt());
                                result.put("username", username);
                                result.put("temporalPassword", password);
                                result.put("message", "Usuario creado exitosamente en Moodle");
                                return result;
                            }
                            throw new RuntimeException("No se pudo crear el usuario en Moodle");
                        });
                    });
            });
    }

    /**
     * Sincroniza un docente con Moodle
     */
    public CompletableFuture<Map<String, Object>> syncDocente(Integer docenteId) {
        return docenteRepository.getById(docenteId)
            .thenCompose(docente -> {
                if (docente == null) {
                    throw new RuntimeException("Docente no encontrado con ID: " + docenteId);
                }

                return moodleClient.getUsersByField("email", docente.getEmail())
                    .thenCompose(existingUsers -> {
                        if (existingUsers.isArray() && existingUsers.size() > 0) {
                            JsonNode user = existingUsers.get(0);
                            Map<String, Object> result = new HashMap<>();
                            result.put("action", "existing");
                            result.put("moodleUserId", user.get("id").asInt());
                            result.put("username", user.get("username").asText());
                            result.put("message", "El docente ya existe en Moodle");
                            return CompletableFuture.completedFuture(result);
                        }

                        String username = generateDocenteUsername(docente.getEmail());
                        String password = generatePassword(username);

                        return moodleClient.createUser(
                            username,
                            password,
                            docente.getNombre(),
                            docente.getApellido(),
                            docente.getEmail()
                        ).thenApply(response -> {
                            if (response.isArray() && response.size() > 0) {
                                JsonNode user = response.get(0);
                                Map<String, Object> result = new HashMap<>();
                                result.put("action", "created");
                                result.put("moodleUserId", user.get("id").asInt());
                                result.put("username", username);
                                result.put("temporalPassword", password);
                                result.put("message", "Docente creado exitosamente en Moodle");
                                return result;
                            }
                            throw new RuntimeException("No se pudo crear el docente en Moodle");
                        });
                    });
            });
    }

    /**
     * Sincroniza una asignatura como curso en Moodle
     */
    public CompletableFuture<Map<String, Object>> syncAsignatura(Integer asignaturaId) {
        return asignaturaRepository.getById(asignaturaId)
            .thenCompose(asignatura -> {
                if (asignatura == null) {
                    throw new RuntimeException("Asignatura no encontrada con ID: " + asignaturaId);
                }

                String shortname = generateCourseShortname(asignatura);
                
                return moodleClient.getCoursesByField("shortname", shortname)
                    .thenCompose(existingCourses -> {
                        if (existingCourses.has("courses") && existingCourses.get("courses").isArray() 
                            && existingCourses.get("courses").size() > 0) {
                            JsonNode course = existingCourses.get("courses").get(0);
                            Map<String, Object> result = new HashMap<>();
                            result.put("action", "existing");
                            result.put("moodleCourseId", course.get("id").asInt());
                            result.put("shortname", course.get("shortname").asText());
                            result.put("message", "El curso ya existe en Moodle");
                            return CompletableFuture.completedFuture(result);
                        }

                        return moodleClient.createCourse(
                            asignatura.getNombre(),
                            shortname,
                            DEFAULT_CATEGORY_ID
                        ).thenApply(response -> {
                            if (response.isArray() && response.size() > 0) {
                                JsonNode course = response.get(0);
                                Map<String, Object> result = new HashMap<>();
                                result.put("action", "created");
                                result.put("moodleCourseId", course.get("id").asInt());
                                result.put("shortname", course.get("shortname").asText());
                                result.put("message", "Curso creado exitosamente en Moodle");
                                return result;
                            }
                            throw new RuntimeException("No se pudo crear el curso en Moodle");
                        });
                    });
            });
    }

    /**
     * Matricula un alumno en un curso de Moodle
     */
    public CompletableFuture<Map<String, Object>> enrolAlumnoToCourse(Integer alumnoId, Integer moodleCourseId) {
        return alumnoRepository.getById(alumnoId)
            .thenCompose(alumno -> {
                if (alumno == null) {
                    throw new RuntimeException("Alumno no encontrado");
                }

                return moodleClient.getUsersByField("email", alumno.getEmail())
                    .thenCompose(users -> {
                        if (!users.isArray() || users.size() == 0) {
                            throw new RuntimeException("Usuario no encontrado en Moodle. Sincroniza primero al alumno.");
                        }

                        int moodleUserId = users.get(0).get("id").asInt();

                        return moodleClient.enrolUser(moodleUserId, moodleCourseId, STUDENT_ROLE_ID)
                            .thenApply(response -> {
                                Map<String, Object> result = new HashMap<>();
                                result.put("message", "Alumno matriculado exitosamente");
                                result.put("moodleUserId", moodleUserId);
                                result.put("moodleCourseId", moodleCourseId);
                                result.put("role", "student");
                                return result;
                            });
                    });
            });
    }

    /**
     * Asigna un docente a un curso en Moodle
     */
    public CompletableFuture<Map<String, Object>> assignDocenteToCourse(Integer docenteId, Integer moodleCourseId) {
        return docenteRepository.getById(docenteId)
            .thenCompose(docente -> {
                if (docente == null) {
                    throw new RuntimeException("Docente no encontrado");
                }

                return moodleClient.getUsersByField("email", docente.getEmail())
                    .thenCompose(users -> {
                        if (!users.isArray() || users.size() == 0) {
                            throw new RuntimeException("Usuario no encontrado en Moodle. Sincroniza primero al docente.");
                        }

                        int moodleUserId = users.get(0).get("id").asInt();

                        return moodleClient.enrolUser(moodleUserId, moodleCourseId, TEACHER_ROLE_ID)
                            .thenApply(response -> {
                                Map<String, Object> result = new HashMap<>();
                                result.put("message", "Docente asignado exitosamente");
                                result.put("moodleUserId", moodleUserId);
                                result.put("moodleCourseId", moodleCourseId);
                                result.put("role", "teacher");
                                return result;
                            });
                    });
            });
    }

    /**
     * Sincroniza todos los alumnos
     */
    public CompletableFuture<Map<String, Object>> syncAllAlumnos() {
        return alumnoRepository.getAll()
            .thenCompose(alumnos -> {
                List<CompletableFuture<Map<String, Object>>> futures = alumnos.stream()
                    .map(alumno -> syncAlumno(alumno.getId())
                        .exceptionally(ex -> {
                            Map<String, Object> error = new HashMap<>();
                            error.put("alumnoId", alumno.getId());
                            error.put("error", ex.getMessage());
                            return error;
                        }))
                    .collect(Collectors.toList());

                return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v -> {
                        List<Map<String, Object>> results = futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList());

                        Map<String, Object> summary = new HashMap<>();
                        summary.put("total", results.size());
                        summary.put("results", results);
                        return summary;
                    });
            });
    }

    // Métodos auxiliares
    private String generatePassword(String base) {
        return base + "2024!";
    }

    private String generateDocenteUsername(String email) {
        return email.split("@")[0].toLowerCase();
    }

    private String generateCourseShortname(Asignatura asignatura) {
        String clean = asignatura.getNombre()
            .replaceAll("[^a-zA-Z0-9]", "")
            .toLowerCase();
        return "C" + asignatura.getCuatrimestre() + "_" + clean.substring(0, Math.min(10, clean.length()));
    }
}