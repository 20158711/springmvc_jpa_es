package org.wingstudio.controller.backend;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wingstudio.common.Const;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.User;
import org.wingstudio.service.IUserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager/user")
@Api(tags = "用户管理")
public class UserManagerController {

    @Autowired
    private IUserService userService;

    @ApiOperation("管理员登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "username",defaultValue = "admin"),
            @ApiImplicitParam(paramType = "query",name = "password",defaultValue = "admin"),
            @ApiImplicitParam(paramType = "query",name = "session")
    })
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password, HttpSession session){
        ServerResponse<User> response=userService.login(username, password);
        if (response.isSuccess()){
            User user=response.getData();
            if (user.getRole()== Const.ROLE.ROLE_ADMIN){
                session.setAttribute(Const.TAG.CURRENT_USER,user);
            }else {
                return ServerResponse.errorMessage("不是管理员");
            }
        }
        return response;
    }

}
