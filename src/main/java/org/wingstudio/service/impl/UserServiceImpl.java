package org.wingstudio.service.impl;

import com.mysql.fabric.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wingstudio.common.Const;
import org.wingstudio.common.ResponseCode;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.common.TokenCache;
import org.wingstudio.dao.UserDao;
import org.wingstudio.po.User;
import org.wingstudio.service.IUserService;
import org.wingstudio.util.MD5Util;
import org.wingstudio.util.UpdateUtil;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    private ServerResponse isExist(String type, String key) {
        if (!Const.TAG.USERNAME.equals(type) && !Const.TAG.EMAIL.equals(type)) {
            return ServerResponse.error();
        } else if (Const.TAG.USERNAME.equals(type) && userDao.countUsersByUsername(key) <= 0) {
            return ServerResponse.errorMessage(Const.ERROR_MSG.USERNAME_NOT_EXISTS);
        } else if (Const.TAG.EMAIL.equals(type) && userDao.countUsersByEmail(key) <= 0) {
            return ServerResponse.errorMessage(Const.ERROR_MSG.EMAIL_NOT_EXISTS);
        } else {
            return ServerResponse.success();
        }
    }

    private ServerResponse isNotExist(String type, String key) {
        if (!Const.TAG.USERNAME.equals(type) && !Const.TAG.EMAIL.equals(type)) {
            return ServerResponse.errorMessage(ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        } else if (Const.TAG.USERNAME.equals(type) && userDao.countUsersByUsername(key) > 0) {
            return ServerResponse.errorMessage(Const.ERROR_MSG.USERNAME_ALREADY_EXISTS);
        } else if (Const.TAG.EMAIL.equals(type) && userDao.countUsersByEmail(key) > 0) {
            return ServerResponse.errorMessage(Const.ERROR_MSG.EMAIL_ALREADY_EXISTS);
        } else {
            return ServerResponse.success();
        }
    }

    @Override
    public ServerResponse<User> login(String username, String password) {
        ServerResponse exist = isExist(Const.TAG.USERNAME, username);
        if (!exist.isSuccess())
            return exist;
        String md5 = MD5Util.MD5EncodeUtf8(password);
        User user = userDao.findUserByUsernameAndPassword(username, md5);
        if (user == null) {
            return ServerResponse.errorMessage(Const.ERROR_MSG.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.success("success", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse notExistUsername = isNotExist(Const.TAG.USERNAME, user.getUsername());
        if (!notExistUsername.isSuccess())
            return notExistUsername;
        ServerResponse notExistEmail = isNotExist(Const.TAG.EMAIL, user.getEmail());
        if (!notExistEmail.isSuccess())
            return notExistEmail;
        user.setRole(Const.ROLE.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        User save = userDao.save(user);
        if (save == null) {
            return ServerResponse.errorMessage("注册失败");
        }
        return ServerResponse.successMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isBlank(type))
            return ServerResponse.errorMessage("参数错误");
        return isNotExist(type, str);
    }

    @Override
    public ServerResponse<String> getQuestionByUsername(String username) {
        ServerResponse exist = isExist(Const.TAG.USERNAME, username);
        if (!exist.isSuccess())
            return exist;
        User user = userDao.findUserByUsername(username);
        if (user != null && StringUtils.isNotBlank(user.getQuestion())) {
            return ServerResponse.success(user.getQuestion());
        }
        return ServerResponse.errorMessage("问题是空的");
    }

    @Override
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        int count = userDao.countUsersByQuestionAndAnswer(question, answer);
        if (count > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.success(forgetToken);
        }
        return ServerResponse.successMessage("问题的答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token) {
        if (StringUtils.isBlank(token)) {
            return ServerResponse.errorMessage("用户不存在存在");
        }
        ServerResponse exist = isExist(Const.TAG.USERNAME, username);
        if (!exist.isSuccess())
            return exist;
        String cacheToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(cacheToken)) {
            return ServerResponse.errorMessage("token无效或过期");
        }
        if (StringUtils.equals(token, cacheToken)) {
            String md5 = MD5Util.MD5EncodeUtf8(passwordNew);
            User oldUser = userDao.findUserByUsername(username);
            oldUser.setPassword(md5);
            User save = userDao.save(oldUser);
            if (save != null) {
                return ServerResponse.successMessage("修改密码成功");
            }
        } else {
            return ServerResponse.errorMessage("token错误");
        }
        return ServerResponse.errorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userDao.countUsersByIdAndPassword(user.getId(), MD5Util.MD5EncodeUtf8(passwordOld));
        if (resultCount == 0) {
            return ServerResponse.errorMessage("旧密码错误");
        }
        User oldUser = userDao.findOne(user.getId());
        oldUser.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        User save = userDao.save(oldUser);
        if (save != null) {
            return ServerResponse.successMessage("update success");
        }
        return ServerResponse.errorMessage("update error");
    }

    @Override
    public ServerResponse<User> updateInfo(User user) {
        int count = userDao.countUsersByEmailAndIdNot(user.getEmail(), user.getId());
        if (count > 0) {
            return ServerResponse.errorMessage("email exists");
        }
        User oldUser = userDao.findOne(user.getId());
        UpdateUtil.copyNotNullProperties(user, oldUser);
        User save = userDao.save(oldUser);
        save.setPassword(null);
        if (save != null) {
            return ServerResponse.success("update success", save);
        }
        return ServerResponse.errorMessage("update error");
    }

    @Override
    public ServerResponse<User> getInfo(Long id) {
        User one = userDao.findOne(id);
        if (one == null) {
            return ServerResponse.errorMessage("用户找不到当前用户");
        }
        one.setPassword(StringUtils.EMPTY);
        return ServerResponse.success(one);
    }

    @Override
    public ServerResponse checkAdmin(HttpSession session) {
        User currentUser= (User) session.getAttribute(Const.TAG.CURRENT_USER);
        if (currentUser==null || currentUser.getId()==null)
            return ServerResponse.errorCodeMessage(ResponseCode.NEED_LOGIN);
        User oldUser = userDao.findOne(currentUser.getId());
        if (oldUser.getRole()==Const.ROLE.ROLE_ADMIN)
            return ServerResponse.success();
        return ServerResponse.errorCodeMessage(ResponseCode.NEED_ADMIN);
    }
}