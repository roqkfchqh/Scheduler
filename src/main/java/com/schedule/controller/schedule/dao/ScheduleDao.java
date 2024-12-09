package com.schedule.controller.schedule.dao;

import com.schedule.common.database.DatabaseExceptionHandler;
import com.schedule.common.database.DatabaseConnection;
import com.schedule.common.exception.CustomException;
import com.schedule.common.exception.ErrorCode;
import com.schedule.controller.schedule.model.Schedule;
import com.schedule.controller.schedule.dto.ScheduleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
@RequiredArgsConstructor
public class ScheduleDao {

    //create
    public void createSchedule(Schedule schedule){
        String sql = "INSERT INTO schedule (id, content, author_id, created, updated) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, schedule.getId());
            pstmt.setString(2, schedule.getContent());
            pstmt.setObject(3, schedule.getAuthor_id());
            pstmt.setTimestamp(4, Timestamp.valueOf(schedule.getCreated()));
            pstmt.setTimestamp(5, Timestamp.valueOf(schedule.getUpdated()));

            pstmt.executeUpdate();

        }catch(SQLException e){
            DatabaseExceptionHandler.sqlExtracted(e);
        }
    }

    //update
    public void updateSchedule(Schedule schedule){
        String sql = "UPDATE schedule SET content = ?, updated = ? WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, schedule.getContent());
            pstmt.setTimestamp(2, Timestamp.valueOf(schedule.getUpdated()));
            pstmt.setObject(3, schedule.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if(rowsUpdated == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            DatabaseExceptionHandler.sqlExtracted(e);
        }
    }

    //delete
    public void deleteSchedule(UUID scheduleId){
        String sql = "DELETE FROM schedule WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, scheduleId);

            int rowsDeleted = pstmt.executeUpdate();
            if(rowsDeleted == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            DatabaseExceptionHandler.sqlExtracted(e);
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
        try(Connection conn = DatabaseConnection.getConnection();
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
                    schedules.add(ScheduleMapper.getBuild(rs));
                }
            }
        } catch (SQLException e) {
            DatabaseExceptionHandler.sqlExtracted(e);
        }
        return schedules;
    }

    //scheduleId 로 조회
    public ScheduleResponseDto findScheduleById(UUID scheduleId){
        String sql = "SELECT s.content, s.created, s.updated ,s.author_id, a.name AS author_name, a.email AS author_email FROM schedule s JOIN author a ON s.author_id = a.id WHERE s.id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, scheduleId);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return ScheduleMapper.getBuild(rs);
                }else{
                    throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
                }
            }
        }catch(SQLException e){
            DatabaseExceptionHandler.sqlExtracted(e);
        }
        return null;
    }
}
