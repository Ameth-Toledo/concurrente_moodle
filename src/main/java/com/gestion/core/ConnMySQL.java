package src.main.java.com.gestion.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnMySQL {
    private final HikariDataSource dataSource;
    private static ConnMySQL instance;

    private ConnMySQL() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String dbHost = getEnvValue(dotenv, "DB_HOST");
        String dbUser = getEnvValue(dotenv, "DB_USER");
        String dbPassword = getEnvValue(dotenv, "DB_PASSWORD");
        String dbName = getEnvValue(dotenv, "DB_NAME");
        String dbPort = getEnvValue(dotenv, "DB_PORT", "3306");
        boolean dbSsl = "true".equals(getEnvValue(dotenv, "DB_SSL", "false"));

        if (dbHost == null || dbUser == null || dbPassword == null || dbName == null) {
            throw new RuntimeException("Error: Faltan variables de entorno. Verifica tu .env");
        }

        HikariConfig config = new HikariConfig();
        
        String jdbcUrl = dbSsl
                ? String.format("jdbc:mysql://%s:%s/%s?useSSL=true&requireSSL=false&verifyServerCertificate=false", 
                    dbHost, dbPort, dbName)
                : String.format("jdbc:mysql://%s:%s/%s?useSSL=false", dbHost, dbPort, dbName);
        
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Configuración del pool
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setKeepaliveTime(30000);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
        
        System.out.println("✅ Pool de conexiones creado");
        testConnection();
    }

    private String getEnvValue(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        return value != null ? value : System.getenv(key);
    }

    private String getEnvValue(Dotenv dotenv, String key, String defaultValue) {
        String value = getEnvValue(dotenv, key);
        return value != null ? value : defaultValue;
    }

    public static synchronized ConnMySQL getInstance() {
        if (instance == null) {
            instance = new ConnMySQL();
        }
        return instance;
    }

    private void testConnection() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT NOW()");
             ResultSet resultSet = statement.executeQuery()) {
            System.out.println("✅ Conexión a MySQL exitosa");
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar la conexión: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("🔒 Pool de conexiones cerrado");
        }
    }
}