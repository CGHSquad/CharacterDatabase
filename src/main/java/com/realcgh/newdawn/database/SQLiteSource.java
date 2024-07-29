package com.realcgh.newdawn.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class SQLiteSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteSource.class);
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        try {
            final File dbFile = new File("characters.db");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Configure HikariCP for SQLite
        config.setJdbcUrl("jdbc:sqlite:characters.db"); // SQLite database file
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);

        try (final Statement statement = getConnection().createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS characters (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "userId VARCHAR(255) NOT NULL," +
                        "name VARCHAR(255) NOT NULL," +
                        "race VARCHAR(255) NOT NULL," +
                        "age VARCHAR(255) NOT NULL,"+
                        "abilities TEXT," +
                        "background TEXT)");
                LOGGER.info("Database initialized and table created if it didn't exist.");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void createCharacter(Connection conn, String userId, String name, String race, String age, String abilities, String background) throws SQLException {
        String sql = "INSERT INTO characters(userId, name, race, age, abilities, background) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userId);
        pstmt.setString(2, name);
        pstmt.setString(3, race);
        pstmt.setString(4, age);
        pstmt.setString(5, abilities);
        pstmt.setString(6, background);
        pstmt.executeUpdate();
    }
}

