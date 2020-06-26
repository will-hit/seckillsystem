package org.softwareb.api.user;

import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Login;
import org.softwareb.entity.User;

public interface IUserService {
    User checkLogin(Login login);

    ResultBean addUser(Login login, User user);

    ResultBean userLogin(Login login);
}
