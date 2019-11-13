/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.dao;

import com.barmagli.helper.MySQLDatabaseHelper;
import com.barmagli.model.FaxImage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FaxImageDaoImpl {

    private MySQLDatabaseHelper db = null;
    private final String table = "fax_image";

    public FaxImageDaoImpl(MySQLDatabaseHelper db) {
        this.db = db;
    }

    public FaxImageDaoImpl() {
        this.db = MySQLDatabaseHelper.getInstance();
    }

    public boolean add(FaxImage faxImage, int id) throws IOException {
        boolean inserted = false;
        try {

            inserted = db.table(table)
                    .data("image_path", faxImage.getImagePath())
                    .data("fax_id", id)
                    .insert();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return inserted;
    }

    public boolean delete(int id) {
        boolean deleted = false;
        try {
            db.table(table)
                    .where("`fax_id`=?", id);
            deleted = db.delete();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return deleted;
    }

    public boolean update(FaxImage faxImage, int id) throws IOException {
        boolean updated = false;
        try {

            updated = db.table(table)
                    .data("image_path", faxImage.getImagePath())
                    .where("`fax_id`=?", id)
                    .update();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return updated;
    }

    public List<FaxImage> getAll() {
        List<FaxImage> faxImages = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .orderBy("id")
                .sort("DESC")
                .fetchData()) {

            while (rs.next()) {
                FaxImage faxImage = new FaxImage();
                faxImage.setId(rs.getInt("id"));
                faxImage.setImagePath(rs.getString("image_path"));
                faxImages.add(faxImage);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return faxImages;
    }

    public List<FaxImage> getLatest(int num) {
        List<FaxImage> faxImages = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .orderBy("id")
                .sort("DESC")
                .limit(num)
                .fetchData()) {

            while (rs.next()) {
                FaxImage faxImage = new FaxImage();

                faxImage.setId(rs.getInt("id"));
                faxImage.setImagePath(rs.getString("image_path"));

                faxImages.add(faxImage);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return faxImages;
    }

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

    public FaxImage getById(long id) {
        FaxImage faxImage = new FaxImage();

        try (ResultSet rs = db.select()
                .table(table)
                .where("`id`=?", id)
                .fetchData()) {

            if (rs.next()) {
                faxImage.setId(rs.getInt("id"));
                faxImage.setImagePath(rs.getString("image_path"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return faxImage;
    }

    public List<FaxImage> getAllByFaxId(long id) {
        List<FaxImage> faxImages = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .where("`fax_id`=?", id)
                .fetchData()) {

            while (rs.next()) {
                FaxImage faxImage = new FaxImage();

                faxImage.setId(rs.getInt("id"));
                faxImage.setImagePath(rs.getString("image_path"));

                faxImages.add(faxImage);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return faxImages;
    }
}
