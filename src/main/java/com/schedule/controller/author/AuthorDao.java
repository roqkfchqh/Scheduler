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

    public String findPassword(UUID id){
        String sql = "SELECT password FROM author WHERE id = ?";

        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, id.toString());

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

    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException{
        Author author = new Author();
        author.setId(UUID.fromString(rs.getString("id")));
        author.setName(rs.getString("name"));
        author.setEmail(rs.getString("email"));
        author.setPassword(rs.getString("password"));
        author.setIp_address(rs.getInt("ip_address"));
        return author;
    }
}
