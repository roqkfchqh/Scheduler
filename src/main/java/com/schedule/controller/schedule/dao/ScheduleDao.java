package com.schedule.controller.schedule.dao;

import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.CustomSQLException;
import com.schedule.controller.common.exception.ErrorCode;
import com.schedule.controller.common.exception.SQLErrorCode;
import com.schedule.controller.schedule.model.Schedule;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
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

    //save
    public void saveSchedule(Schedule schedule){
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

    //update
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
            sqlExtracted(e);
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
            sqlExtracted(e);
        }
    }

    //모두조회 (paging 용)
    public List<ScheduleResponseDto> findAllSchedule(String authorName, LocalDate date){
        String sql = "SELECT s.id, s.content, s.created, s.updated, s.author_id, a.name AS author_name, a.email AS author_email " +
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

        List<ScheduleResponseDto> schedules = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            int index = 1;
            if(authorName != null){
                pstmt.setString(index++, "%" + authorName + "%");
            }
            if(date != null){
                pstmt.setDate(index++, Date.valueOf(date));
                pstmt.setDate(index++, Date.valueOf(date));
            }

            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    schedules.add(getBuild(rs));
                }
            }
        } catch (SQLException e) {
            sqlExtracted(e);
        }
        return schedules;
    }

    //scheduleId 로 조회
    public ScheduleResponseDto findScheduleById(UUID scheduleId){
        String sql = "SELECT s.content, s.created, s.updated ,s.author_id, a.name AS author_name, a.email AS author_email FROM schedule s JOIN author a ON s.author_id = a.id WHERE s.id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, scheduleId);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return getBuild(rs);
                }else{
                    throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
                }
            }
        }catch(SQLException e){
            sqlExtracted(e);
        }
        return null;
    }

    //responseDto 빌더
    private static ScheduleResponseDto getBuild(ResultSet rs) throws SQLException {
        return ScheduleResponseDto.builder()
                .authorId((UUID)rs.getObject("author_id"))
                .content(rs.getString("content"))
                .authorName(rs.getString("author_name"))
                .authorEmail(rs.getString("author_email"))
                .created(rs.getTimestamp("created").toLocalDateTime().toString())
                .updated(rs.getTimestamp("updated").toLocalDateTime().toString())
                .build();
    }

    //sql 예외처리 한꺼번에 처리
    private static void sqlExtracted(SQLException e){
        logger.error("SQL Exception 발생: SQLState={}, ErrorCode={}, Message={}",
                e.getSQLState(), e.getErrorCode(), e.getMessage(), e);

        if(e.getSQLState().startsWith("08")){
            throw new CustomSQLException(SQLErrorCode.DATABASE_CONNECTION_ERROR);
        }else if(e.getSQLState().startsWith("22")){
            throw new CustomSQLException(SQLErrorCode.DATA_TYPE_ERROR);
        }else{
            throw new CustomSQLException(SQLErrorCode.UNKNOWN_DATABASE_ERROR);
        }
    }
}