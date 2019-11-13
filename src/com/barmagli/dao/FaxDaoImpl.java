/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barmagli.dao;

import com.barmagli.helper.MySQLDatabaseHelper;
import com.barmagli.model.FaxImage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.barmagli.model.Fax;
import com.barmagli.model.FaxPlace;
import java.sql.Date;

public class FaxDaoImpl {

    private MySQLDatabaseHelper db = null;
    private final String table = "fax";
    public int lastFaxAddedId = 0;

    public FaxDaoImpl(MySQLDatabaseHelper db) {
        this.db = db;
    }

    public FaxDaoImpl() {
        this.db = MySQLDatabaseHelper.getInstance();
    }

    public boolean update(Fax fax) {
        boolean updated = false;
        try {
            db.table(table)
                    .data("about", fax.getAbout())
                    .data("date", fax.getDate())
                    .data("send_number", fax.getSendNumber())
                    .data("saved_number", fax.getSavedNumber())
                    .data("state", fax.getState())
                    .data("type", fax.getType());
            if (fax.getFaxRef() != 0) {
                db.data("fax_id", fax.getFaxRef());
            }
            updated = db.where("`id`=?", fax.getId()).update();

            List<FaxPlace> places = fax.getFaxPlaces();
            for (FaxPlace place : places) {
                db.table("fax_place")
                        .data("name", place.getName())
                        .where("`fax_id`=?", fax.getId())
                        .update();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return updated;
    }

    public boolean add(Fax fax) {
        boolean inserted = false;
        try {
            db.table(table)
                    .data("about", fax.getAbout())
                    .data("date", fax.getDate())
                    .data("send_number", fax.getSendNumber())
                    .data("saved_number", fax.getSavedNumber())
                    .data("state", fax.getState())
                    .data("type", fax.getType());
            if (fax.getFaxRef() != 0) {
                db.data("fax_id", fax.getFaxRef());
            }
            inserted = db.insert();

            lastFaxAddedId = db.getLastId();

            List<FaxPlace> places = fax.getFaxPlaces();
            for (FaxPlace place : places) {
                db.table("fax_place")
                        .data("name", place.getName())
                        .data("fax_id", lastFaxAddedId)
                        .insert();
            }
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
                    .where("`id`=?", id);

            deleted = db.delete();

            db.table("fax_place")
                    .where("`fax_id`=?", id)
                    .delete();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return deleted;
    }

    /**
     * get all users data table database
     *
     * @return found users
     */
    public List<Fax> getAll() {
        List<Fax> faxes = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .orderBy("id")
                .sort("DESC")
                .fetchData()) {

            while (rs.next()) {
                Fax fax = new Fax();

                fillFaxFromResultSet(fax, rs);

                faxes.add(fax);
            }
            for (Fax fax : faxes) {
                ResultSet rsPlaces = db.select()
                        .table("fax_place")
                        .where("`fax_id`=?", fax.getId())
                        .fetchData();
                while (rsPlaces.next()) {
                    FaxPlace p = new FaxPlace(rsPlaces.getInt("id"), rsPlaces.getString("name"));
                    fax.getFaxPlaces().add(p);
                }

                ResultSet rsImages = db.select()
                        .table("fax_image")
                        .where("`fax_id`=?", fax.getId())
                        .fetchData();
                while (rsImages.next()) {
                    FaxImage img = new FaxImage(rsImages.getInt("id"), rsImages.getString("image_path"));
                    fax.getFaxImages().add(img);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return faxes;
    }

    /**
     * get latest users data table database depending on number
     *
     * @param num
     * @return latest num users
     */
    public List<Fax> getLatest(int num) {
        List<Fax> faxes = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .orderBy("id")
                .sort("DESC")
                .limit(num)
                .fetchData()) {

            while (rs.next()) {
                Fax fax = new Fax();

                fillFaxFromResultSet(fax, rs);;

                faxes.add(fax);
            }
            for (Fax fax : faxes) {
                ResultSet rsPlaces = db.select()
                        .table("fax_place").where("`fax_id`=?", fax.getId()).fetchData();
                while (rsPlaces.next()) {
                    FaxPlace p = new FaxPlace(rsPlaces.getInt("id"), rsPlaces.getString("name"));
                    fax.getFaxPlaces().add(p);
                }

                ResultSet rsImages = db.select()
                        .table("fax_image")
                        .where("`fax_id`=?", fax.getId())
                        .fetchData();
                while (rsImages.next()) {
                    FaxImage img = new FaxImage(rsImages.getInt("id"), rsImages.getString("image_path"));
                    fax.getFaxImages().add(img);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return faxes;
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
    public Fax getById(long id) {
        return getBy("`id`=?", id);
    }

    public List<Fax> getAllById(long id) {
        return getAllBy("`id`=?", id);
    }

    /**
     * get specific user with id
     *
     * @param sendNumber
     * @return found user
     */
    public Fax getBySendNumber(long sendNumber) {
        return getBy("`send_number`=?", sendNumber);
    }

    /**
     * get specific user with id
     *
     * @param faxRef
     * @return found user
     */
    public Fax getByFaxRef(long faxRef) {
        return getBy("`fax_id`=?", faxRef);
    }

    public List<Fax> getAllByFaxRef(long faxRef) {
        return getAllBy("`fax_id`=?", faxRef);
    }

    public List<Fax> getByDate(Date date) {
        return getAllBy("`date` BETWEEN ? AND ?", date, date);
    }

    public List<Fax> getAllByDate(Date from, Date to) {
        return getAllBy("`date` BETWEEN ? AND ?", from, to);
    }

    public List<Fax> getAllBySavedNumber(String savedNumber) {
        return getAllBy("`saved_number`=?", savedNumber);
    }

    public List<Fax> getAllByAbout(String about) {
        return getAllBy("`about` LIKE ?", "%" + about + "%");
    }

    public List<Fax> getAllBy(String where, Object... wheres) {
        List<Fax> faxes = new ArrayList();

        try (ResultSet rs = db.select()
                .table(table)
                .where(where, wheres)
                .fetchData()) {
            while (rs.next()) {
                Fax fax = new Fax();

                fillFaxFromResultSet(fax, rs);

                faxes.add(fax);
            }
            for (Fax fax : faxes) {
                System.out.println(fax);
                ResultSet rsPlaces = db.select()
                        .table("fax_place")
                        .where("`fax_id`=?", fax.getId())
                        .fetchData();

                while (rsPlaces.next()) {
                    FaxPlace p = new FaxPlace(rsPlaces.getInt("id"), rsPlaces.getString("name"));
                    fax.getFaxPlaces().add(p);
                }

                ResultSet rsImages = db.select()
                        .table("fax_image")
                        .where("`fax_id`=?", fax.getId())
                        .fetchData();
                while (rsImages.next()) {
                    FaxImage img = new FaxImage(rsImages.getInt("id"), rsImages.getString("image_path"));
                    fax.getFaxImages().add(img);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return faxes;
    }

    public Fax getBy(String where, Object... wheres) {
        Fax fax = new Fax();
        try (ResultSet rs = db.select()
                .table(table)
                .where(where, wheres)
                .fetchData()) {
            if (rs.next()) {
                fillFaxFromResultSet(fax, rs);
            }

            ResultSet rsPlaces = db.select()
                    .table("fax_place")
                    .where("`fax_id`=?", fax.getId())
                    .fetchData();
            while (rsPlaces.next()) {
                FaxPlace p = new FaxPlace(rsPlaces.getInt("id"), rsPlaces.getString("name"));
                fax.getFaxPlaces().add(p);
            }

            ResultSet rsImages = db.select()
                    .table("fax_image")
                    .where("`fax_id`=?", fax.getId())
                    .fetchData();
            while (rsImages.next()) {
                FaxImage img = new FaxImage(rsImages.getInt("id"), rsImages.getString("image_path"));
                fax.getFaxImages().add(img);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return fax;
    }

    public void fillFaxFromResultSet(Fax fax, ResultSet rs) throws SQLException {

        fax.setId(rs.getInt("id"));
        fax.setAbout(rs.getString("about"));
        fax.setDate(rs.getDate("date"));
        fax.setSendNumber(rs.getInt("send_number"));
        fax.setSavedNumber(rs.getString("saved_number"));
        fax.setFaxRef(rs.getInt("fax_id"));
        fax.setType(rs.getBoolean("type"));
        fax.setState(rs.getBoolean("state"));

        fax.setFaxPlaces(new ArrayList());
        fax.setFaxImages(new ArrayList());
    }

    public List<Fax> getAllBySendFor(String sendFor) {
        return getAllBy("`send_for`=?", sendFor);
    }

    public List<Fax> getAllBySendForAndDate(String sendFor, Date from, Date to) {
        return getAllBy("`send_for`=? AND `date` BETWEEN ? AND ?", sendFor, from, to);
    }

    public List<Fax> getAllBySendForAndDate(String sendFor, Date date) {
        return getAllBy("`send_for`=? AND `date`=?", sendFor, date);
    }

    public List<Fax> getAllBySendForAndSavedNumber(String sendFor, String savedNumber) {
        return getAllBy("`send_for`=? AND `saved_number`=?", sendFor, savedNumber);
    }

    public List<Fax> getAllBySendForAndSendNumberAndSavedNumber(String sendFor, String sendNumber, String savedNumber) {
        return getAllBy("`send_for`=? AND `send_number`=? AND `saved_number`=?", sendFor, sendNumber, savedNumber);
    }

    public List<Fax> getAllBySavedNumberAndDate(String savedNumber, Date from, Date to) {
        return getAllBy("`saved_number`=? AND `date` BETWEEN ? AND ?", savedNumber, from, to);
    }

    public List<Fax> getAllBySavedNumberAndDate(String savedNumber, Date date) {
        return getAllBy("`saved_number`=? AND `date`=?", savedNumber, date);
    }

    public Fax getAllBysendNumberAndDate(long sendNumber, Date from, Date to) {
        return getBy("`send_number`=? AND `date` BETWEEN ? AND ?", sendNumber, from, to);
    }

    public Fax getBySendNumberAndDate(long sendNumber, Date date) {
        return getBy("`send_number`=? AND `date`=?", sendNumber, date);
    }

    public Fax getBySendNumberSavedNumberAndDate(long sendNumber, String SavedNumber) {
        return getBy("`send_number`=? AND `saved_number`=?", SavedNumber);
    }

    public Fax getBySendNumberAndSavedNumberAndDate(long sendNumber, String savedNumber, Date from, Date to) {
        return getBy("`send_number`=? AND `saved_number`=? AND `date` BETWEEN ? AND ?", sendNumber, savedNumber, from, to);
    }

    public Fax getBySendNumberAndSavedNumberAndDate(long sendNumber, String savedNumber, Date date) {
        return getBy("`send_number`=? AND `saved_number`=? AND `date`=?", sendNumber, savedNumber, date);
    }

    String where = "";
    List<Object> bindings = new ArrayList();

    public FaxDaoImpl sendNumber(long sendNumber) {
        if (sendNumber != 0) {
            if (this.where.isEmpty()) {
                this.where += "`send_number`=?";
            } else {
                this.where += " AND `send_number`=?";
            }
            bindings.add(sendNumber);
        }
        return this;
    }

    public FaxDaoImpl place(String sendFor) {
        if (sendFor != null && !sendFor.isEmpty()) {
            if (this.where.isEmpty()) {
                this.where += "`send_for`=?";
            } else {
                this.where += " AND `send_for`=?";
            }
            bindings.add(sendFor);
        }
        return this;
    }

    public FaxDaoImpl savedNumber(String savedNumber) {
        if (savedNumber != null && !savedNumber.isEmpty()) {
            if (this.where.isEmpty()) {
                this.where += "`saved_number`=?";
            } else {
                this.where += " AND `saved_number`=?";
            }
            bindings.add(savedNumber);
        }
        return this;
    }

    public FaxDaoImpl dateFromTo(Date from, Date to) {
        if (from != null) {

            bindings.add(from);
            if (to != null) {
                bindings.add(to);
            } else {
                bindings.add(from);
            }

            if (this.where.isEmpty()) {
                this.where += "`date` BETWEEN ? AND ?";
            } else {
                this.where += " AND `date` BETWEEN ? AND ?";
            }
        }
        return this;
    }

    public FaxDaoImpl dateFrom(Date from) {
        if (from != null) {

            bindings.add(from);

            if (this.where.isEmpty()) {
                this.where += "`date` BETWEEN ?";
            } else {
                this.where += " AND `date` BETWEEN ?";
            }
        }
        return this;
    }

    public FaxDaoImpl dateTo(Date to) {
        if (to != null) {
            bindings.add(to);
            this.where += " AND ?";
        }
        return this;
    }

    public List<Fax> fetch() {
        return getAllBy(where, bindings.toArray());
    }

    public List<Fax> getAllByAdvanceSearch(String where, Object... wheres) {
        List<Fax> faxes = new ArrayList();

        try (ResultSet rs = db.select("`fax`.`id`", "`fax`.`about`", "`fax`.`date`", "`fax`.`send_number`", "`fax`.`saved_number`", "`fax`.`fax_id`", "`fax`.`type`", "`fax`.`state`")
                .table("`fax`, `fax_place`")
                .where(where, wheres)
                .fetchData()) {
            while (rs.next()) {
                Fax fax = new Fax();

                fillFaxFromResultSet(fax, rs);

                faxes.add(fax);
            }
            for (Fax fax : faxes) {
                System.out.println(fax);
                ResultSet rsPlaces = db.select()
                        .table("fax_place")
                        .where("`fax_id`=?", fax.getId())
                        .fetchData();

                while (rsPlaces.next()) {
                    FaxPlace p = new FaxPlace(rsPlaces.getInt("id"), rsPlaces.getString("name"));
                    fax.getFaxPlaces().add(p);
                }

                ResultSet rsImages = db.select()
                        .table("fax_image")
                        .where("`fax_id`=?", fax.getId())
                        .fetchData();
                while (rsImages.next()) {
                    FaxImage img = new FaxImage(rsImages.getInt("id"), rsImages.getString("image_path"));
                    fax.getFaxImages().add(img);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return faxes;
    }
}
