package com.schedule.controller.schedule;

import com.schedule.controller.common.BadInputException;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ScheduleDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/schedule_db2";
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    //save
    public void save(Schedule schedule){
        String sql = "INSERT INTO schedule (id, name, content, password, created, updated) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            pstmt.setObject(1, schedule.getId());
            pstmt.setString(2, schedule.getName());
            pstmt.setString(3, schedule.getContent());
            pstmt.setString(4, schedule.getPassword());
            pstmt.setTimestamp(5, Timestamp.valueOf(schedule.getCreated()));
            pstmt.setTimestamp(6, Timestamp.valueOf(schedule.getUpdated()));

            pstmt.executeUpdate();

        }catch(SQLException e){
            throw new BadInputException("오류요" + e.getMessage());
        }
    }

    //update
    public void update(Schedule schedule){
        String sql = "UPDATE schedule SET name = ?, content = ?, updated = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, schedule.getName());
            pstmt.setString(2, schedule.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(schedule.getUpdated()));
            pstmt.setObject(4, schedule.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if(rowsUpdated == 0){
                throw new BadInputException("오류요");
            }

        }catch(SQLException e){
            throw new BadInputException("오류요" + e.getMessage());
        }
    }

    //delete
    public void deleteByID(UUID id){
        String sql = "DELETE FROM schedule WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if(rowsDeleted == 0){
                throw new BadInputException("오류요");
            }

        }catch(SQLException e){
            throw new BadInputException("오류요" + e.getMessage());
        }
    }

    //모두조회
    public List<Schedule> findAll(String name, LocalDate date){
        String sql = "SELECT id, name, content, password, created, updated FROM schedule WHERE 1=1";
        List<Schedule> schedules = new ArrayList<>();

        if(name != null){
            sql += " AND name = ?";
        }
        if(date != null){
            sql += " AND (DATE(created) = ? OR DATE(updated) = ?)";
        }
        sql += " ORDER BY created DESC";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int index = 1;
            if (name != null) {
                pstmt.setString(index++, name);
            }
            if (date != null) {
                pstmt.setDate(index++, Date.valueOf(date));
                pstmt.setDate(index++, Date.valueOf(date));
            }

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        }catch(SQLException e) {
            throw new BadInputException("오류요" + e.getMessage());
        }
        return schedules;
    }

    //id 로 조회
    public Schedule findById(UUID id){
        String sql = "SELECT id, name, content, password, created, updated FROM schedule WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToSchedule(rs);
                }else{
                    throw new BadInputException("오류요");
                }
            }
        }catch(SQLException e){
            throw new BadInputException("오류요" + e.getMessage());
        }
    }

    //resultSet -> schedule mapping
    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException{
        Schedule schedule = new Schedule();
        schedule.setId((UUID)rs.getObject("id"));
        schedule.setName(rs.getString("name"));
        schedule.setContent(rs.getString("content"));
        schedule.setPassword(rs.getString("password"));
        schedule.setCreated(rs.getTimestamp("created").toLocalDateTime());
        schedule.setUpdated(rs.getTimestamp("updated").toLocalDateTime());
        return schedule;
    }
}
