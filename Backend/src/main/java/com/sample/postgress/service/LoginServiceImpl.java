package com.sample.postgress.service;

import com.sample.postgress.dao.EmployeeDao;
import com.sample.postgress.dao.UserInfoDao;
import com.sample.postgress.entity.Employee;
import com.sample.postgress.model.UserInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginServiceImpl implements LoginService {
    @Resource
    UserInfoDao userInfoDao;
    @Override
    public boolean signUp(UserInfo userInfo) {
        return  userInfoDao.signUp(userInfo);
    }
    @Override
    public boolean login(UserInfo userInfo){
       return userInfoDao.login(userInfo);
    }
}
