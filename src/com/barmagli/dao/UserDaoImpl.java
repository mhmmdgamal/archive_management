/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.dao;

import com.barmagli.helper.MySQLDatabaseHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.barmagli.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl {

    private MySQLDatabaseHelper db = null;
    private final String table = "user";

    public UserDaoImpl(MySQLDatabaseHelper db) {
        this.db = db;
    }

    public UserDaoImpl() {
        this.db = MySQLDatabaseHelper.getInstance();
    }

    public boolean updateIsAutologin(boolean isAutologin, User user) {
        boolean updated = false;
        try {
            updated = db.table(table)
                    .data("is_auto_login", isAutologin)
                    .where("`id`=?", user.getId())
                    .update();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return updated;
    }

    public boolean updatePermessoin(boolean isAdmin, User user) {
        boolean updated = false;
        try {
            updated = db.table(table)
                    .data("is_admin", isAdmin)
                    .where("`id`=?", user.getId())
                    .update();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return updated;
    }

    public boolean addUser(User user) {
        boolean inserted = false;
        try {
            inserted = db.table(table)
                    .data("username", user.getUsername())
                    .data("password", user.getPassword())
                    .data("is_auto_login", 0)
                    .data("is_admin", user.getIsAdmin())
                    .insert();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return inserted;
    }

    public boolean updateUser(User user, int id) {
        boolean updated = false;
        try {
            updated = db.table(table)
                    .data("username", user.getUsername())
                    .data("password", user.getPassword())
                    .data("is_auto_login", 0)
                    .data("is_admin", user.getIsAdmin())
                    .where("`id`=?", id)
                    .update();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return updated;
    }

    public User getByIsAutoLogin() {
        return getBy("`is_auto_login`=1");
    }

    public User getByUsernameAndPassword(String username, String password) {
        return getBy("`username`=? AND `password`=?", username, password);
    }

    public User getByUsername(String username) {
        return getBy("`username`=?", username);
    }

    public User getBy(String where, Object... wheres) {
        User user = new User();
        try (ResultSet rs = db.select()
                .table(table)
                .where(where, wheres)
                .fetchData()) {
            if (rs.next()) {
                fillFaxFromResultSet(user, rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return user;
    }

    public List<User> getAll() {
        List<User> users = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .orderBy("id")
                .sort("DESC")
                .fetchData()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setIsAdmin(rs.getBoolean("is_admin"));
                users.add(user);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return users;
    }

    public void fillFaxFromResultSet(User user, ResultSet rs) throws SQLException {

        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setIsAutoLogin(rs.getBoolean("is_auto_login"));
        user.setIsAdmin(rs.getBoolean("is_admin"));
    }

    public boolean deleteUser(String username) {
        boolean deleted = false;
        try {
            deleted = db.table(table)
                    .where("`username`=?", username)
                    .delete();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return deleted;
    }
}
