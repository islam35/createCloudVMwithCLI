package com.sample.postgress.dao;

import com.sample.postgress.entity.Employee;
import com.sample.postgress.model.UserInfo;

public interface UserInfoDao {

    boolean signUp(UserInfo emp);
    boolean login(UserInfo emp);
}
