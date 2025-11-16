package com.gestion.Moodle.infrastructure.client;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.Moodle.config.MoodleConfig;

public class MoodleClient {
    private final MoodleConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public MoodleClient() {
        this.config = MoodleConfig.getInstance();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Ejecuta una función de Moodle Web Service
     */
    public CompletableFuture<JsonNode> executeFunction(String functionName, Map<String, String> params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!config.isConfigured()) {
                    throw new RuntimeException("Moodle no está configurado. Verifica MOODLE_TOKEN en .env");
                }

                // Construir parámetros
                StringBuilder paramsBuilder = new StringBuilder();
                paramsBuilder.append("wstoken=").append(config.getMoodleToken());
                paramsBuilder.append("&wsfunction=").append(functionName);
                paramsBuilder.append("&moodlewsrestformat=").append(config.getWsFormat());

                if (params != null && !params.isEmpty()) {
                    String encodedParams = params.entrySet().stream()
                            .map(e -> encodeParam(e.getKey(), e.getValue()))
                            .collect(Collectors.joining("&"));
                    paramsBuilder.append("&").append(encodedParams);
                }

                // Crear request
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(config.getWebServiceUrl()))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(paramsBuilder.toString()))
                        .timeout(Duration.ofSeconds(30))
                        .build();

                // Ejecutar request
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new RuntimeException("Error HTTP " + response.statusCode() + ": " + response.body());
                }

                // Parsear respuesta JSON
                JsonNode jsonResponse = objectMapper.readTree(response.body());

                // Verificar errores de Moodle
                if (jsonResponse.has("exception")) {
                    String exception = jsonResponse.get("exception").asText();
                    String message = jsonResponse.has("message") ? jsonResponse.get("message").asText() : "Error desconocido";
                    throw new RuntimeException("Error de Moodle [" + exception + "]: " + message);
                }

                return jsonResponse;

            } catch (Exception e) {
                throw new RuntimeException("Error al ejecutar función de Moodle: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Obtiene información del sitio Moodle
     */
    public CompletableFuture<JsonNode> getSiteInfo() {
        return executeFunction("core_webservice_get_site_info", null);
    }

    /**
     * Crea un usuario en Moodle
     */
    public CompletableFuture<JsonNode> createUser(String username, String password, String firstname, 
                                                   String lastname, String email) {
        Map<String, String> params = Map.of(
            "users[0][username]", username,
            "users[0][password]", password,
            "users[0][firstname]", firstname,
            "users[0][lastname]", lastname,
            "users[0][email]", email
        );
        return executeFunction("core_user_create_users", params);
    }

    /**
     * Crea un curso en Moodle
     */
    public CompletableFuture<JsonNode> createCourse(String fullname, String shortname, int categoryId) {
        Map<String, String> params = Map.of(
            "courses[0][fullname]", fullname,
            "courses[0][shortname]", shortname,
            "courses[0][categoryid]", String.valueOf(categoryId)
        );
        return executeFunction("core_course_create_courses", params);
    }

    /**
     * Matricula un usuario en un curso
     */
    public CompletableFuture<JsonNode> enrolUser(int userId, int courseId, int roleId) {
        Map<String, String> params = Map.of(
            "enrolments[0][roleid]", String.valueOf(roleId),
            "enrolments[0][userid]", String.valueOf(userId),
            "enrolments[0][courseid]", String.valueOf(courseId)
        );
        return executeFunction("enrol_manual_enrol_users", params);
    }

    /**
     * Obtiene usuarios por campo
     */
    public CompletableFuture<JsonNode> getUsersByField(String field, String value) {
        Map<String, String> params = Map.of(
            "field", field,
            "values[0]", value
        );
        return executeFunction("core_user_get_users_by_field", params);
    }

    /**
     * Obtiene cursos por campo
     */
    public CompletableFuture<JsonNode> getCoursesByField(String field, String value) {
        Map<String, String> params = Map.of(
            "field", field,
            "value", value
        );
        return executeFunction("core_course_get_courses_by_field", params);
    }

    /**
     * Verifica la conexión con Moodle
     */
    public CompletableFuture<Boolean> testConnection() {
        return getSiteInfo()
                .thenApply(response -> response.has("sitename"))
                .exceptionally(ex -> false);
    }

    private String encodeParam(String key, String value) {
        try {
            return URLEncoder.encode(key, StandardCharsets.UTF_8) + "=" + 
                   URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al codificar parámetro: " + e.getMessage(), e);
        }
    }
}