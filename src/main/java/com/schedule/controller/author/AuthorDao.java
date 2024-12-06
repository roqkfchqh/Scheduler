package com.schedule.controller.author;

import com.schedule.controller.common.exception.CustomException;
import com.schedule.controller.common.exception.ErrorCode;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.UUID;

@Repository
public class AuthorDao {
    private static final String URL ="jdbc:postgresql://localhost:5432/schedule_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public void createAuthor(Author author){
        String sql = "INSERT INTO author (id, email, name, password) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            pstmt.setObject(1, author.getId());
            pstmt.setString(2, author.getEmail());
            pstmt.setString(3, author.getName());
            pstmt.setString(4, author.getPassword());

            pstmt.executeUpdate();

        }catch(SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    public void updateAuthor(Author author){
        String sql = "UPDATE author SET name = ?, password = ?, email = ? WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getPassword());
            pstmt.setString(3, author.getEmail());
            pstmt.setObject(4, author.getId());


            int rowsUpdated = pstmt.executeUpdate();
            if(rowsUpdated == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    public void deleteAuthor(UUID authorId){
        String sql = "DELETE FROM author WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, authorId);

            int rowsDeleted = pstmt.executeUpdate();
            if(rowsDeleted == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    public Author findAuthorById(UUID authorId){
        String sql = "SELECT id, name, email, password FROM author WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setObject(1, authorId);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToAuthor(rs);
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

    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException{
        Author author = new Author();
        author.setId((UUID) rs.getObject("id"));
        author.setName(rs.getString("name"));
        author.setEmail(rs.getString("email"));
        author.setPassword(rs.getString("password"));
        return author;
    }
}
