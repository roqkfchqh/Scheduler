package com.schedule.controller.schedule;

import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.CustomSQLException;
import com.schedule.controller.common.exception.ErrorCode;
import com.schedule.controller.common.exception.SQLErrorCode;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ScheduleDao {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleDao.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/schedule_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    //saveSchedule
    public void saveSchedule(Schedule schedule) throws CustomSQLException {
        String sql = "INSERT INTO schedule (id, content, author_id, created, updated) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            pstmt.setObject(1, schedule.getId());
            pstmt.setString(2, schedule.getContent());
            pstmt.setObject(3, schedule.getAuthor_id());
            pstmt.setTimestamp(4, Timestamp.valueOf(schedule.getCreated()));
            pstmt.setTimestamp(5, Timestamp.valueOf(schedule.getUpdated()));

            pstmt.executeUpdate();

        }catch(SQLException e){
            sqlExtracted(e);
        }
    }

    //updateSchedule
    public void updateSchedule(Schedule schedule) throws CustomSQLException {
        String sql = "UPDATE schedule SET content = ?, updated = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, schedule.getContent());
            pstmt.setTimestamp(2, Timestamp.valueOf(schedule.getUpdated()));
            pstmt.setObject(3, schedule.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if(rowsUpdated == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            sqlExtracted(e);
        }
    }

    //delete
    public void deleteSchedule(UUID scheduleId) throws CustomSQLException {
        String sql = "DELETE FROM schedule WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, scheduleId);

            int rowsDeleted = pstmt.executeUpdate();
            if(rowsDeleted == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            sqlExtracted(e);
        }
    }

    //모두조회
    public List<Schedule> findAllSchedule(String authorName, LocalDate date) throws CustomSQLException {
        String sql = "SELECT s.id, s.content, s.created, s.updated, s.author_id, s.author_email " +
                "FROM schedule s " +
                "JOIN author a ON s.author_id = a.id " +
                "WHERE 1=1";

        if(authorName != null){
            sql += " AND a.name LIKE ?";
        }
        if(date != null){
            sql += " AND (DATE(s.created) = ? OR DATE(s.updated) = ?)";
        }
        sql += " ORDER BY s.created DESC";

        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int index = 1;
            if (authorName != null) {
                pstmt.setString(index++, "%" + authorName + "%");
            }
            if(date != null){
                pstmt.setDate(index++, Date.valueOf(date));
                pstmt.setDate(index++, Date.valueOf(date));
            }

            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        }catch(SQLException e){
            sqlExtracted(e);
        }
        return schedules;
    }

    //id 로 조회
    public Schedule findScheduleById(UUID scheduleId) throws CustomSQLException {
        String sql = "SELECT id, content, author_id, created, updated FROM schedule WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, scheduleId);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToSchedule(rs);
                }else{
                    throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
                }
            }
        }catch(SQLException e){
            sqlExtracted(e);
        }
        return null;
    }

    //resultSet -> schedule mapping
    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException{
        Schedule schedule = new Schedule();
        schedule.setId((UUID) rs.getObject("id"));
        schedule.setContent(rs.getString("content"));
        schedule.setAuthor_id((UUID)(rs.getObject("author_id")));
        schedule.setCreated(rs.getTimestamp("created").toLocalDateTime());
        schedule.setUpdated(rs.getTimestamp("updated").toLocalDateTime());
        return schedule;
    }

    private static void sqlExtracted(SQLException e) throws CustomSQLException {
        logger.error("SQL Exception 발생: SQLState={}, ErrorCode={}, Message={}",
                e.getSQLState(), e.getErrorCode(), e.getMessage(), e);

        if(e.getSQLState().startsWith("08")){
            throw new CustomSQLException(SQLErrorCode.DATABASE_CONNECTION_ERROR, e);
        }else if(e.getSQLState().startsWith("22")){
            throw new CustomSQLException(SQLErrorCode.DATA_TYPE_ERROR, e);
        }else{
            throw new CustomSQLException(SQLErrorCode.UNKNOWN_DATABASE_ERROR, e);
        }
    }
}
