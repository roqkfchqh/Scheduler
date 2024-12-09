package com.schedule.controller.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//db 커넥션
public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/schedule_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
