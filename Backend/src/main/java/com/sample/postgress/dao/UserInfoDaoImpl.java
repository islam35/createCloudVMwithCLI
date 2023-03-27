package com.sample.postgress.dao;

import com.sample.postgress.entity.Employee;
import com.sample.postgress.mapper.EmployeeRowMapper;
import com.sample.postgress.mapper.UserInfoMapper;
import com.sample.postgress.model.UserInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserInfoDaoImpl implements UserInfoDao{
    public UserInfoDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    NamedParameterJdbcTemplate template;
    @Override
    public boolean signUp(UserInfo userInfo) {
        final String sql = "insert into userInfo(userEmail, password ) values(:userEmail,:password)";

        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userEmail", userInfo.getEmail())
                .addValue("password", userInfo.getPassword());
        try{
            template.update(sql,param, holder);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean login(UserInfo userInfo){

        final String sql = "select * from userInfo where userEmail=:userEmail AND password=:password";


        Map<String,Object> map=new HashMap<String,Object>();
        map.put("userEmail", userInfo.getEmail());
        map.put("password", userInfo.getPassword());

        List<UserInfo> user = template.query(sql, map, new UserInfoMapper());


        if (user.isEmpty() )
            return false;
        return  true;
    }


}
