package org.softwareb.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.softwareb.api.user.IUserService;
import org.softwareb.api.user.pojo.UserVO;
import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Login;
import org.softwareb.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class SSOController {

    @Reference
    private IUserService userService;

    @RequestMapping("/register")
    public ResultBean register(UserVO vo){
        System.out.println(Integer.MAX_VALUE);
        Login login  = new Login(vo.getUsername(), vo.getPassword());
        User user = new User(vo.getName(), vo.getGender(), vo.getPhone(),
                vo.getMail(), vo.getType());
        ResultBean res = userService.addUser(login, user);
        return res;
    }

    @RequestMapping("/checkLogin")
    public ResultBean checkLogin(Login login){
        if (login == null){
            return ResultBean.error("用户未登录",500);
        }
        User user = userService.checkLogin(login);
        if(user == null){
            return ResultBean.error("不存在此用户",501);
        }else{
            return ResultBean.success(null);
        }
    }

    @RequestMapping("/login")
    public ResultBean login(Login login){
        ResultBean resultBean = userService.userLogin(login);
        return resultBean;
    }


}
