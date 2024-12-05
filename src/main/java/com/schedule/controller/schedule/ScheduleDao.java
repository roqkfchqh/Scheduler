package com.schedule.controller.schedule;

import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.ErrorCode;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ScheduleDao {

    private static final String URL = "jdbc:postgresql://localhost:5432/schedule_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    //saveSchedule
    public void saveSchedule(Schedule schedule){
        String sql = "INSERT INTO schedule (id, content, author_id, created, updated) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            pstmt.setObject(1, schedule.getId());
            pstmt.setString(2, schedule.getContent());
            pstmt.setString(3, schedule.getAuthor_id().toString());
            pstmt.setTimestamp(4, Timestamp.valueOf(schedule.getCreated()));
            pstmt.setTimestamp(5, Timestamp.valueOf(schedule.getUpdated()));

            pstmt.executeUpdate();

        }catch(SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    //updateSchedule
    public void updateSchedule(Schedule schedule){
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
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    //delete
    public void deleteSchedule(UUID scheduleId){
        String sql = "DELETE FROM schedule WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, scheduleId);

            int rowsDeleted = pstmt.executeUpdate();
            if(rowsDeleted == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    //모두조회
    public List<Schedule> findAllSchedule(String authorName, LocalDate date){
        String sql = "SELECT id, name, content, password, created, updated FROM schedule WHERE 1=1";
        List<Schedule> schedules = new ArrayList<>();

        if(authorName != null){
            sql += " AND name = ?";
        }
        if(date != null){
            sql += " AND (DATE(created) = ? OR DATE(updated) = ?)";
        }
        sql += " ORDER BY created DESC";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int index = 1;
            if (authorName != null) {
                pstmt.setString(index++, authorName);
            }
            if (date != null) {
                pstmt.setDate(index++, Date.valueOf(date));
                pstmt.setDate(index++, Date.valueOf(date));
            }

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    schedules.add(mapResultSetToSchedule(rs));
                }
                if(!rs.next()){
                    throw new CustomException(ErrorCode.BAD_GATEWAY);
                }
            }
        }catch(SQLException e) {
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
        return schedules;
    }

    //id 로 조회
    public Schedule findScheduleById(UUID scheduleId){
        String sql = "SELECT id, content, author_id, created, updated FROM schedule WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, scheduleId);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToSchedule(rs);
                }
                if(!rs.next()){
                    throw new CustomException(ErrorCode.BAD_GATEWAY);
                }
            }
        }catch(SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
        return null;
    }

    //resultSet -> schedule mapping
    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException{
        Schedule schedule = new Schedule();
        schedule.setId((UUID) rs.getObject("id"));
        schedule.setContent(rs.getString("content"));
        schedule.setAuthor_id(UUID.fromString(rs.getString("author_id")));
        schedule.setCreated(rs.getTimestamp("created").toLocalDateTime());
        schedule.setUpdated(rs.getTimestamp("updated").toLocalDateTime());
        return schedule;
    }
}
