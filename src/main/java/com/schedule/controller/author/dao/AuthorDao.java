package com.schedule.controller.author.dao;

import com.schedule.controller.author.dto.AuthorResponseDto;
import com.schedule.controller.author.model.Author;
import com.schedule.common.database.DatabaseExceptionHandler;
import com.schedule.common.database.DatabaseConnection;
import com.schedule.common.exception.CustomException;
import com.schedule.common.exception.ErrorCode;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class AuthorDao {

    //create
    public void createAuthor(Author author){
        String sql = "INSERT INTO author (id, email, name, password, created, updated) VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, author.getId());
            pstmt.setString(2, author.getEmail());
            pstmt.setString(3, author.getName());
            pstmt.setString(4, author.getPassword());
            pstmt.setTimestamp(5, Timestamp.valueOf(author.getCreated()));
            pstmt.setTimestamp(6, Timestamp.valueOf(author.getUpdated()));

            pstmt.executeUpdate();

        }catch(SQLException e){
            DatabaseExceptionHandler.sqlExtracted(e);
        }
    }

    //update
    public void updateAuthor(Author author){
        String sql = "UPDATE author SET name = ?, password = ?, email = ?, updated = ? WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getPassword());
            pstmt.setString(3, author.getEmail());
            pstmt.setTimestamp(4, Timestamp.valueOf(author.getUpdated()));
            pstmt.setObject(5, author.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if(rowsUpdated == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            DatabaseExceptionHandler.sqlExtracted(e);
        }
    }

    //delete
    public void deleteAuthor(UUID authorId){
        String sql = "DELETE FROM author WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, authorId);

            int rowsDeleted = pstmt.executeUpdate();
            if(rowsDeleted == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            DatabaseExceptionHandler.sqlExtracted(e);
        }
    }

    //authorId로 조회
    public AuthorResponseDto findAuthorById(UUID authorId) {
        String sql = "SELECT a.id, a.name, a.email, a.password, a.created, a.updated, s.content AS schedule_content " +
                "FROM author a LEFT JOIN schedule s ON a.id = s.author_id " +
                "WHERE a.id = ? AND s.is_deleted = false";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, authorId);

            try (ResultSet rs = pstmt.executeQuery()) {
                AuthorResponseDto.AuthorResponseDtoBuilder builder = null;
                List<String> scheduleContents = new ArrayList<>();

                while(rs.next()){
                    if(builder == null){
                        builder = AuthorResponseDto.builder()
                                .id((UUID) rs.getObject("id"))
                                .name(rs.getString("name"))
                                .email(rs.getString("email"))
                                .password(rs.getString("password"))
                                .created(rs.getTimestamp("created").toLocalDateTime().toString())
                                .updated(rs.getTimestamp("updated").toLocalDateTime().toString());
                    }
                    String content = rs.getString("schedule_content");
                    if(content != null){
                        scheduleContents.add(content);
                    }
                }

                if(builder != null){
                    return builder.content(scheduleContents).build();
                }else{
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }
            }
        }catch(SQLException e){
            DatabaseExceptionHandler.sqlExtracted(e);
        }
        return null;
    }
}
