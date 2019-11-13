/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.dao;

import com.barmagli.helper.MySQLDatabaseHelper;
import com.barmagli.model.FaxPlace;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FaxPlaceDaoImpl {

    private MySQLDatabaseHelper db = null;
    private final String table = "fax_place";

    public FaxPlaceDaoImpl(MySQLDatabaseHelper db) {
        this.db = db;
    }

    public FaxPlaceDaoImpl() {
        this.db = MySQLDatabaseHelper.getInstance();
    }

    /**
     * add user
     *
     * @param place
     * @return true if added false otherwise
     */
    public boolean add(FaxPlace place) {
        boolean inserted = false;
        try {
            inserted = db.table(table)
                    .data("name", place.getName())
                    .insert();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return inserted;
    }
    
    /**
     * add user
     *
     * @param place
     * @return true if added false otherwise
     */
    public boolean update(FaxPlace place) {
        boolean updateed = false;
        try {
            updateed = db.table(table)
                    .data("name", place.getName())
                    .update();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return updateed;
    }

    /**
     * get all users data table database
     *
     * @return found FaxesIn
     */
    public List<FaxPlace> getAll() {
        List<FaxPlace> places = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .orderBy("id")
                .sort("DESC")
                .fetchData()) {

            while (rs.next()) {
                FaxPlace place = new FaxPlace();
                place.setId(rs.getInt("id"));
                place.setName(rs.getString("name"));
                places.add(place);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return places;
    }

    /**
     * get latest users data table database depending on number
     *
     * @param num
     * @return latest num users
     */
    public List<FaxPlace> getLatest(int num) {
        List<FaxPlace> places = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .orderBy("id")
                .sort("DESC")
                .limit(num)
                .fetchData()) {

            while (rs.next()) {
                FaxPlace place = new FaxPlace();

                place.setId(rs.getInt("id"));
                place.setName(rs.getString("name"));

                places.add(place);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return places;
    }

    /**
     * get number of users
     *
     * @return users number or 0
     */
    public int getNum() {
        int count = 0;
        try {
            ResultSet rs = db.select("COUNT(id)")
                    .table(table)
                    .fetchData();

            if (rs.next()) {
                return rs.getInt("COUNT(id)");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return count;
    }

    /**
     * get specific user with id
     *
     * @param id
     * @return found user
     */
    public FaxPlace getById(long id) {
        FaxPlace place = new FaxPlace();

        try (ResultSet rs = db.select()
                .table(table)
                .where("`id`=?", id)
                .fetchData()) {

            if (rs.next()) {
                place.setId(rs.getInt("id"));
                place.setName(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return place;
    }
}
