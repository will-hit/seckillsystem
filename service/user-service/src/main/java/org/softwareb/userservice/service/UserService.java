package org.softwareb.userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.softwareb.api.user.IUserService;
import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Login;
import org.softwareb.entity.User;
import org.softwareb.mapper.LoginMapper;
import org.softwareb.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Service
public class UserService implements IUserService {

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public User checkLogin(Login login) {
        Login login1 = loginMapper.selectByPrimaryKey(login.getUsername());
        if (login1 != null){
            boolean res = passwordEncoder.matches(login.getPassword(), login1.getPassword());
            if (res){
                User user = userMapper.selectByPrimaryKey(login1.getUid());
                return user;
            }
        }
        return null;
    }

    // 注册使用
    @Override
    @Transactional
    public ResultBean addUser(Login login, User user) {
        // 加密处理
        String encode = passwordEncoder.encode(login.getPassword());
        login.setPassword(encode);
        try{
            int col = loginMapper.insert(login);
            if (col == 1){
                userMapper.insert(user);
                login.setUid(user.getId());
                loginMapper.updateByPrimaryKey(login);
            }else{
                return ResultBean.error("数据库发生错误！", 500);
            }
        }catch (DuplicateKeyException e){
            return ResultBean.error("用户名已存在！", 500);
        }
        return ResultBean.success(null);
    }

    @Override
    public ResultBean userLogin(Login login) {
        try {
            if (login == null) {
                return ResultBean.error("用户为空！", 501);
            } else {
                Login login1 = loginMapper.selectByPrimaryKey(login.getUsername());
                if (login1 != null){
                    boolean res = passwordEncoder.matches(login.getPassword(), login1.getPassword());
                    if (res) return ResultBean.success(null);
                }
            }
        } catch (Exception e) {
            return ResultBean.error("网络异常！", 500);
        }
        return ResultBean.error("用户名或者密码错误!", 502);
    }

}
