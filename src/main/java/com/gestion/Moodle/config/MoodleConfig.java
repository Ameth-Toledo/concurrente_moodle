package com.gestion.Moodle.config;

import io.github.cdimascio.dotenv.Dotenv;

public class MoodleConfig {
    private static MoodleConfig instance;
    
    private final String moodleUrl;
    private final String moodleToken;
    private final String wsFormat;

    private MoodleConfig() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        this.moodleUrl = getEnvValue(dotenv, "MOODLE_URL");
        this.moodleToken = getEnvValue(dotenv, "MOODLE_TOKEN");
        this.wsFormat = "json";

        if (this.moodleToken == null || this.moodleToken.isEmpty()) {
            System.err.println("⚠️  ADVERTENCIA: MOODLE_TOKEN no está configurado en .env");
        }
    }

    public static synchronized MoodleConfig getInstance() {
        if (instance == null) {
            instance = new MoodleConfig();
        }
        return instance;
    }

    private String getEnvValue(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        return value != null ? value : System.getenv(key);
    }

    private String getEnvValue(Dotenv dotenv, String key, String defaultValue) {
        String value = getEnvValue(dotenv, key);
        return value != null ? value : defaultValue;
    }

    public String getMoodleUrl() {
        return moodleUrl;
    }

    public String getMoodleToken() {
        return moodleToken;
    }

    public String getWsFormat() {
        return wsFormat;
    }

    public String getWebServiceUrl() {
        return moodleUrl + "/webservice/rest/server.php";
    }

    public boolean isConfigured() {
        return moodleToken != null && !moodleToken.isEmpty();
    }

    @Override
    public String toString() {
        return "MoodleConfig{" +
                "moodleUrl='" + moodleUrl + '\'' +
                ", tokenConfigured=" + isConfigured() +
                ", wsFormat='" + wsFormat + '\'' +
                '}';
    }
}