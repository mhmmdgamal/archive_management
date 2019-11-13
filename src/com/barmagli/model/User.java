/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.model;

import com.barmagli.helper.Helper;

/**
 *
 * @author MohamedGamal
 */
public class User implements Formatable {

    private int id;
    private String username;
    private String password;
    private boolean isAutoLogin;
    private boolean isAdmin;
    private boolean isLastLogin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsAutoLogin() {
        return isAutoLogin;
    }

    public void setIsAutoLogin(boolean isAutoLogin) {
        this.isAutoLogin = isAutoLogin;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public boolean getIsLastLogin() {
        return isLastLogin;
    }

    public void setIsLastLogin(boolean isLastLogin) {
        this.isLastLogin = isLastLogin;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + ", isAutoLogin=" + isAutoLogin + ", isAdmin=" + isAdmin + ", isLastLogin=" + isLastLogin + '}';
    }

    @Override
    public Object[] formateToTable() {
        String userData[] = new String[]{
            "" + this.id,
            this.username,
            this.password,
            (this.isAdmin) ? "مسئول" : "مستخدم"
        };
        return userData;
    }
}
