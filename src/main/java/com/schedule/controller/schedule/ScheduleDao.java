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
    private static final String URL = "jdbc:postgresql://localhost:5432/schedule_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    //save
    public void save(Schedule schedule){
        String sql =
                "INSERT INTO Schedule (id, name, content, password, created, updated) VALUES (?,?,?,?,?,?)";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1, schedule.getId().toString());
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
        String sql = "UPDATE Schedule SET name = ?, content = ?, password = ?, updated = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, schedule.getName());
            pstmt.setString(2, schedule.getContent());
            pstmt.setString(3, schedule.getPassword());
            pstmt.setTimestamp(4, Timestamp.valueOf(schedule.getUpdated()));
            pstmt.setString(5, schedule.getId().toString());

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
        String sql = "DELETE FROM Schedule WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, id.toString());

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
        String sql = "SELECT id, name, content, password, created, updated FROM Schedule WHERE 1=1";
        List<Schedule> schedules = new ArrayList<>();

        if(name != null){
            sql += " AND name = ?";
        }
        if(date != null){
            sql += " AND (DATE(created) = ? OR DATE(updated) = ?)";
        }
        sql += " ORDER BY created DESC";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

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
        String sql = "SELECT id, name, content, password, created, updated FROM Schedule WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, id.toString());

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToSchedule(rs);
                }
            }
        }catch(SQLException e){
            throw new BadInputException("오류요" + e.getMessage());
        }
        return null;
    }

    //resultSet -> schedule mapping
    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException{
        Schedule schedule = new Schedule();
        schedule.setId(UUID.fromString(rs.getString("id")));
        schedule.setName(rs.getString("name"));
        schedule.setContent(rs.getString("content"));
        schedule.setPassword(rs.getString("password"));
        schedule.setCreated(rs.getTimestamp("created").toLocalDateTime());
        schedule.setUpdated(rs.getTimestamp("updated").toLocalDateTime());
        return schedule;
    }
}
