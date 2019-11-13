/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.model;

import com.barmagli.dao.UserDaoImpl;
import java.util.List;

/**
 *
 * @author MohamedGamal
 */
public class DatabaseUserOperation {

    public static final UserDaoImpl USER_DAO = new UserDaoImpl();
    public static User currentUser;

    public static User getUser(String username, String password) {

        User user = USER_DAO.getByUsernameAndPassword(username, password);
        if (user.getId() == 0) {
            return null;
        }
        return user;
    }

    public static void updateUserIsAutoLogin(boolean isAutoLogin, User user) {
        USER_DAO.updateIsAutologin(isAutoLogin, user);
    }

    public static User getByIsAutoLogin() {
        return USER_DAO.getByIsAutoLogin();
    }

    public static List<User> getAllUsers() {
        return USER_DAO.getAll();
    }

    public static User getByUsername(String username) {
        return USER_DAO.getByUsername(username);
    }

    public static boolean addUser(User user) {
        return USER_DAO.addUser(user);
    }

    public static boolean updateUser(User user, int id) {
        return USER_DAO.updateUser(user, id);
    }

    public static boolean deleteByUsername(String username) {
        return USER_DAO.deleteUser(username);
    }

}
