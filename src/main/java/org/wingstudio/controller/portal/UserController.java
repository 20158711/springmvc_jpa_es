package org.wingstudio.controller.portal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wingstudio.common.Const;
import org.wingstudio.common.ResponseCode;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.User;
import org.wingstudio.service.IUserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", defaultValue = "yanbi"),
            @ApiImplicitParam(paramType = "query", name = "password", defaultValue = "123456"),
            @ApiImplicitParam(paramType = "query", name = "session", defaultValue = "")
    })
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session) {
        ServerResponse<User> login = userService.login(username, password);
        if (login.isSuccess()) {
            session.setAttribute(Const.TAG.CURRENT_USER, login.getData());
        }
        return login;
    }

    @ApiOperation("用户登出")
    @ApiImplicitParam(paramType = "query", name = "session", defaultValue = "")
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.TAG.CURRENT_USER);
        return ServerResponse.success();
    }

    @ApiOperation(value = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", required = true, dataType = "String", defaultValue = "yanbing"),
            @ApiImplicitParam(paramType = "query", name = "password", required = true, dataType = "String", defaultValue = "123456"),
            @ApiImplicitParam(paramType = "query", name = "email", required = true, dataType = "String", defaultValue = "1280334378@qq.com"),
            @ApiImplicitParam(paramType = "query", name = "phone", required = true, dataType = "String", defaultValue = "18728194099"),
            @ApiImplicitParam(paramType = "query", name = "question", required = true, dataType = "String", defaultValue = "question"),
            @ApiImplicitParam(paramType = "query", name = "answer", required = true, dataType = "String", defaultValue = "answer")
    })
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }

    @ApiOperation("校验用户名或邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "str", defaultValue = "yanbing"),
            @ApiImplicitParam(paramType = "query", name = "type", defaultValue = "username")
    })
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(
            @RequestParam("str") String str,
            @RequestParam("type") String type) {
        return userService.checkValid(str, type);
    }

    @ApiOperation("获取找回密码问题")
    @ApiImplicitParam(paramType = "query", name = "username", defaultValue = "yanbing")
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(@RequestParam("username") String username) {
        return userService.getQuestionByUsername(username);
    }

    @ApiOperation("验证答案是否正确")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", defaultValue = "yanbing"),
            @ApiImplicitParam(paramType = "query", name = "question", defaultValue = "question"),
            @ApiImplicitParam(paramType = "query", name = "answer", defaultValue = "answer")
    })
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(
            @RequestParam("username") String username,
            @RequestParam("question") String question,
            @RequestParam("answer") String answer) {
        return userService.forgetCheckAnswer(username, question, answer);
    }

    @ApiOperation("忘记密码重置")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", defaultValue = "yanbing"),
            @ApiImplicitParam(paramType = "query", name = "passwordNew", defaultValue = "123456"),
            @ApiImplicitParam(paramType = "query", name = "token", defaultValue = "")
    })
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(
            @RequestParam("username") String username,
            @RequestParam("passwordNew") String passwordNew,
            @RequestParam("token") String token) {
        return userService.forgetResetPassword(username, passwordNew, token);
    }

    @ApiOperation("登录时密码重置")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "passwordOld", defaultValue = "123456"),
            @ApiImplicitParam(paramType = "query", name = "passwordNew", defaultValue = "123456"),
            @ApiImplicitParam(paramType = "query", name = "session", defaultValue = "")
    })
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,
                                                @RequestParam("passwordOld") String passwordOld,
                                                @RequestParam("passwordNew") String passwordNew) {
        User user = (User) session.getAttribute(Const.TAG.CURRENT_USER);
        if (user == null) {
            return ServerResponse.errorMessage("用户未登录");
        }
        return userService.resetPassword(passwordOld, passwordNew, user);
    }

    @ApiOperation("更新个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", defaultValue = "1234567890"),
            @ApiImplicitParam(paramType = "query", name = "session", defaultValue = "")
    })
    @RequestMapping(value = "update_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_info(HttpSession session, @ModelAttribute User user) {
        User currUser = (User) session.getAttribute(Const.TAG.CURRENT_USER);
        if (currUser == null) {
            return ServerResponse.errorMessage("用户未登录");
        }
        user.setId(currUser.getId());
        user.setUsername(currUser.getUsername());
        ServerResponse<User> response = userService.updateInfo(user);
        if (response.isSuccess())
            session.setAttribute(Const.TAG.CURRENT_USER, response.getData());
        return response;
    }


    @ApiOperation("获取用户信息")
    @ApiImplicitParam(paramType = "query", name = "session", defaultValue = "")
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.TAG.CURRENT_USER);
        if (user != null) {
            return ServerResponse.success(user);
        }
        return ServerResponse.errorMessage("用户未登录");
    }

    @ApiOperation("获取个人信息")
    @ApiImplicitParam(paramType = "query", name = "session", defaultValue = "")
    @RequestMapping(value = "get_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfo(HttpSession session) {
        User currUser = (User) session.getAttribute(Const.TAG.CURRENT_USER);
        if (currUser == null) {
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        }
        return userService.getInfo(currUser.getId());
    }

}
