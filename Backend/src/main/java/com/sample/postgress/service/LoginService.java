package com.sample.postgress.service;

import com.sample.postgress.entity.Employee;
import com.sample.postgress.model.UserInfo;

public interface LoginService {

    boolean signUp(UserInfo userInfo);
    boolean login(UserInfo userInfo);
}
