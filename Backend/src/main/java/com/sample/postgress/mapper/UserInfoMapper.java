package com.sample.postgress.mapper;

import com.sample.postgress.entity.Employee;
import com.sample.postgress.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoMapper implements RowMapper<UserInfo> {
    @Override
    public UserInfo mapRow(ResultSet rs, int arg1) throws SQLException {
        UserInfo userInfo = new UserInfo("","");
        userInfo.setEmail(rs.getString("userEmail"));
        userInfo.setPassword(rs.getString("password"));

        return userInfo;
    }
}
