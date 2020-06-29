package org.softwareb.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.softwareb.api.user.IUserService;
import org.softwareb.api.user.pojo.UserVO;
import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Login;
import org.softwareb.entity.User;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class SSOController {

    @Reference
    private IUserService userService;

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public ResultBean register(@RequestBody UserVO vo){
        System.out.println(vo);
        Login login  = new Login(vo.getUsername(), vo.getPassword());
        User user = new User(vo.getName(), vo.getGender(), vo.getPhone(),
                vo.getMail(), vo.getType());
        ResultBean res = userService.addUser(login, user);
        return res;
    }

    @PostMapping(value="/checkLogin", produces = "application/json;charset=UTF-8")
    public ResultBean checkLogin(@RequestBody Login login){
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

    @PostMapping(value="/login", produces = "application/json;charset=UTF-8")
    public ResultBean login(@RequestBody Login login){
        System.out.println(login);
        ResultBean resultBean = userService.userLogin(login);
        return resultBean;
    }


}
