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
        String sql = "INSERT INTO author (id, ip_adress, email, name, password) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1, author.getId().toString());
            pstmt.setString(2, author.getIpAddress());
            pstmt.setString(3, author.getEmail());
            pstmt.setString(4, author.getName());
            pstmt.setString(5, author.getPassword());

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

            pstmt.setString(1, authorId.toString());

            int rowsDeleted = pstmt.executeUpdate();
            if(rowsDeleted == 0){
                throw new CustomException(ErrorCode.BAD_GATEWAY);
            }

        }catch(SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
    }

    public String findPassword(UUID authorId){
        String sql = "SELECT password FROM author WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, authorId.toString());

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getString("password");
                }
            }
        }catch (SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
        return null;
    }

    public Author findAuthorById(UUID authorId){
        String sql = "SELECT id, name, email FROM author WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, authorId.toString());

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return mapResultSetToAuthor(rs);
                }
            }
        }catch(SQLException e){
            throw new CustomException(ErrorCode.BAD_GATEWAY);
        }
        return null;
    }

    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException{
        Author author = new Author();
        author.setId(UUID.fromString(rs.getString("id")));
        author.setName(rs.getString("name"));
        author.setEmail(rs.getString("email"));
        author.setPassword(rs.getString("password"));
        author.setIpAddress(rs.getString("ip_address"));
        return author;
    }
}
