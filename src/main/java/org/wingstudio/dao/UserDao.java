package org.wingstudio.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.po.User;

public interface UserDao extends JpaRepository<User,Long> {
    int countUsersByUsername(String username);
    int countUsersByEmail(String email);
    int countUsersByQuestionAndAnswer(String question,String answer);
    int countUsersByIdAndPassword(Long id,String password);
    int countUsersByEmailAndIdNot(String email,Long id);
    User findUserByUsernameAndPassword(String username,String password);
    User findUserByUsername(String username);
}
