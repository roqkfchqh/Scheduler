package com.schedule.controller.schedule.dao;

import com.schedule.controller.schedule.dto.ScheduleResponseDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ScheduleMapper {

    //responseDto 빌더
    public static ScheduleResponseDto getBuild(ResultSet rs) throws SQLException {
        return ScheduleResponseDto.builder()
                .authorId((UUID)rs.getObject("author_id"))
                .content(rs.getString("content"))
                .authorName(rs.getString("author_name"))
                .authorEmail(rs.getString("author_email"))
                .created(rs.getTimestamp("created").toLocalDateTime().toString())
                .updated(rs.getTimestamp("updated").toLocalDateTime().toString())
                .build();
    }
}
