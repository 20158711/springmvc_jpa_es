package org.wingstudio.service;

import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.User;

import javax.servlet.http.HttpSession;

public interface IUserService {
    ServerResponse<User> login(String username,String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> getQuestionByUsername(String username);

    ServerResponse<String> forgetCheckAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInfo(User user);

    ServerResponse<User> getInfo(Long id);

    ServerResponse checkAdmin(HttpSession session);
}
