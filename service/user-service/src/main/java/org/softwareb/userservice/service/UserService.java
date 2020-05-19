package org.softwareb.userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.softwareb.api.user.IUserService;

@Service
public class UserService implements IUserService {

    @Override
    public String test() {
        return "Hello World!";
    }
}
