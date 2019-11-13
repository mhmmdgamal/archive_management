package com.barmagli.helper;

//<editor-fold >
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
//</editor-fold >

public final class MySQLDatabaseHelper {

    /**
     * Table Name
     *
     * @var string
     */
    private String table;

    /**
     * Data Container to set key and Value, for example :
     * pstmt.setString(key,Value);
     *
     * @var map
     */
    private Map<String, Object> data = new LinkedHashMap();

    /**
     * Bindings Container to set Value, for example :
     * pstmt.setString(key,Value);
     *
     * @var array
     */
    private ArrayList bindings = new ArrayList();

    /**
     * Wheres (condition)
     *
     * @var array
     */
    private ArrayList wheres = new ArrayList();

    /**
     * Selects(SELECT column1, column2, ...)
     *
     * @var array
     */
    private ArrayList<String> selects = new ArrayList();

    /**
     * select first 30 records : "SELECT * FROM Orders LIMIT 30
     *
     * @var int
     */
    private int limit;

    /**
     * start select with Offset : "SELECT * FROM Orders LIMIT 30 OFFSET 15";
     *
     * @var int
     */
    private int offset;

    /**
     * Joins
     *
     * @var array
     */
    private ArrayList<String> joins = new ArrayList();

    /**
     * Order By
     *
     * @array
     */
    private ArrayList<String> orderBy = new ArrayList();

    /**
     * Order By
     *
     * @var string
     */
    private String sort;

    /**
     * last id
     *
     * @var int
     */
    private int lastId;

    /**
     * Connection
     *
     * @var Connection
     */
    private Connection connection = null;

    /**
     * singleton instance of Database
     */
    private static MySQLDatabaseHelper instance = null;

    /**
     * connect to database
     *
     * @param url
     * @param userName
     * @param password
     */
    private MySQLDatabaseHelper(String url, String userName, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
            Logger.getLogger(MySQLDatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            if (ex.getMessage().startsWith("Unknown database")) {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.7/", "archive", "archive");
                    Statement stmt = conn.createStatement();
                    stmt.execute("DROP DATABASE IF EXISTS archive_management");
                    stmt.execute("CREATE DATABASE archive_management charset=utf8 collate utf8_general_ci");
                    DatabaseHelper.restoreDatabaseFromSQL(false);
                    this.connection = DriverManager.getConnection(url, userName, password);
                } catch (SQLException ex1) {
                    System.out.println(ex1);
                }
            } else if (ex.getMessage().startsWith("Communications link failure")) {
                JOptionPane.showMessageDialog(null, "يوجد مشاكل بالاتصال \n"
                        + "تأكد من اتصال الشبكة \n وتشغيل سرفر الداتابيس", "تنبيه", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            System.out.println(ex);
        }
    }

    /**
     * get singleton instance of MySQLDatabaseHelper
     *
     * @param url
     * @param userName
     * @param password
     * @return MySQLDatabaseHelper
     */
    public static MySQLDatabaseHelper getInstance(String url, String userName, String password) {
        if (instance == null) {
            instance = new MySQLDatabaseHelper(url, userName, password);
        }
        return instance;
    }

    /**
     * get singleton instance of MySQLDatabaseHelper
     *
     * @return MySQLDatabaseHelper
     */
    public static MySQLDatabaseHelper getInstance() {
        return instance;
    }

    /**
     * close connection
     *
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Get Database Connection Object PDO Object(php)
     *
     * @return Connection
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Set select clause
     *
     * @param selects
     * @return this
     */
    public MySQLDatabaseHelper select(String... selects) {
        this.selects.addAll(Arrays.asList(selects));
        return this;
    }

    /**
     * Set Join clause (item.userID=user.id;)
     *
     * @param join
     * @return this
     */
    public MySQLDatabaseHelper join(String join) {
        this.joins.add(join);
        return this;
    }

    /**
     * Set Limit
     *
     * @param limit
     * @return this
     */
    public MySQLDatabaseHelper limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Set and offset
     *
     * @param offset
     * @return this
     */
    public MySQLDatabaseHelper offset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * Set Order By clause
     *
     * @param orderBy
     * @return this
     */
    public MySQLDatabaseHelper orderBy(String orderBy) {
        this.orderBy.add(orderBy);
        return this;
    }

    /**
     * Set sort clause
     *
     * @param sort
     * @return this
     */
    public MySQLDatabaseHelper sort(String sort) {
        if (sort.isEmpty()) {
            sort = "ASC";
        }
        this.sort = sort;
        return this;
    }

    /**
     * Fetch Records table Table
     *
     * @return ResultSet | null
     * @throws java.sql.SQLException
     */
    public ResultSet fetchData() throws SQLException {

        String query = this.fetchStatement();
        System.out.println(query);
        PreparedStatement ps = this.buildQuery(query, this.bindings);
        this.reset();
        ResultSet results = ps.executeQuery();

        return results;
    }

    /**
     * Prepare fetch Statement
     *
     * @return string
     */
    private String fetchStatement() {
        String query = "SELECT ";
        this.selects = Helper.replaceSpacesWithBlanck(this.selects);
        this.selects.removeAll(Arrays.asList(null, ""));
        if (Helper.isNotBlank(this.selects)) {
            query += String.join(", ", this.selects);
        } else {
            query += "* ";
        }
        query += " FROM " + this.table + " ";
        if (Helper.isNotBlank(this.joins)) {
            query += String.join(" ", this.joins);
        }
        if (Helper.isNotBlank(this.wheres)) {
            query += " WHERE " + String.join(" AND ", this.wheres);
            query = Helper.rTrim(query, "AND ");
        }
        if (Helper.isNotBlank(this.orderBy)) {
            query += " ORDER BY " + String.join(", ", this.orderBy);
        }
        if (Helper.isNotBlank(this.sort)) {
            query += " " + sort;
        }
        if (this.limit != 0) {
            query += " LIMIT " + this.limit;
        }
        if (this.offset != 0) {
            query += " OFFSET " + this.offset;
        }

        return query;
    }

    /**
     * Set the table name
     *
     * @param table
     * @return this
     */
    public MySQLDatabaseHelper table(String table) {
        this.table = table;
        return this;
    }

    /**
     * Delete Clause
     *
     * @return this
     * @throws java.sql.SQLException
     */
    public boolean delete() throws SQLException {

        String query = "DELETE FROM " + this.table + " ";
        if (Helper.isNotBlank(this.wheres)) {
            query += " WHERE " + String.join(" ", this.wheres);
        }
        System.out.println(query);
        PreparedStatement ps = this.buildQuery(query, this.bindings);
        this.reset();
        int rowsAffected = ps.executeUpdate();
        return (rowsAffected > 0);
    }

    /**
     * Set The Data that will be stored in database table
     *
     * @param key
     * @param value
     * @return this
     */
    public MySQLDatabaseHelper data(String key, Object value) {
        this.data.put(key, value);
        this.setBindings(value);
        return this;
    }

    /**
     * Insert Data to database
     *
     * @return this
     * @throws java.sql.SQLException
     */
    public boolean insert() throws SQLException {

        String query = "INSERT INTO " + this.table + " SET ";
        query += this.setFields();
        System.out.println(query);
        PreparedStatement ps = this.buildQuery(query, this.bindings);
        this.reset();
        int rowsAffected = ps.executeUpdate();
        this.generateLastId(ps);
        return (rowsAffected > 0);
    }

    /**
     * Update Data In database
     *
     * @return this
     * @throws java.sql.SQLException
     */
    public boolean update() throws SQLException {

        String query = "UPDATE " + this.table + " SET ";
        query += this.setFields();
        if (Helper.isNotBlank(this.wheres)) {
            query += " WHERE " + String.join(" ", this.wheres);
        }
        PreparedStatement ps = this.buildQuery(query, this.bindings);
        this.reset();
        int rowsAffected = ps.executeUpdate();
        this.generateLastId(ps);
        return (rowsAffected > 0);
    }

    /**
     * Set the fields for insert and update, for example:PreparedStatement pstmt
     * = con.prepareStatement ("UPDATE employee SET name = ? WHERE id = ?");
     *
     * @return string (query)
     */
    private String setFields() {
        String query = "";
        for (String key : this.data.keySet()) {
            query += "`" + key + "` = ? , ";
        }
        query = Helper.rTrim(query, ", ");
        return query;
    }

    /**
     * Add New Where clause
     *
     * @param query
     * @param wheres
     * @return this
     */
    public MySQLDatabaseHelper where(String query, Object... wheres) {
        this.setBindings(wheres);
        this.wheres.add(query);
        return this;
    }

    /**
     * Add New Where clause
     *
     * @param query
     * @return this
     */
    public MySQLDatabaseHelper where(String query) {
        this.wheres.add(query);
        return this;
    }

    /**
     * Execute the given buildQuery statement for example :
     * pstmt.setString(1,"Ahmad"); pstmt.setInt(2, 12);
     *
     * @param query ("UPDATE employee SET name = ? WHERE id = ?")
     * @param bindings
     * @return PreparedStatement object
     * @throws java.sql.SQLException
     */
    public PreparedStatement buildQuery(String query, ArrayList bindings) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int count = 1;
        for (Object binding : this.bindings) {
            if (binding instanceof String) {
                ps.setString(count, (String) binding);
                ++count;
            } else if (binding instanceof Byte) {
                ps.setByte(count, (byte) binding);
                ++count;
            } else if (binding instanceof Short) {
                ps.setShort(count, (short) binding);
                ++count;
            } else if (binding instanceof Integer) {
                ps.setInt(count, (int) binding);
                ++count;
            } else if (binding instanceof Long) {
                ps.setLong(count, (long) binding);
                ++count;
            } else if (binding instanceof Float) {
                ps.setFloat(count, (float) binding);
                ++count;
            } else if (binding instanceof Double) {
                ps.setDouble(count, (double) binding);
                ++count;
            } else if (binding instanceof Date) {
                ps.setDate(count, (Date) binding);
                ++count;
            } else if (binding instanceof Boolean) {
                ps.setBoolean(count, (Boolean) binding);
                ++count;
            } else if (binding instanceof FileInputStream) {
                FileInputStream fis = (FileInputStream) binding;
                try {
                    ps.setBinaryStream(count, fis, fis.available());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                ++count;
            }
        }
        return ps;
    }

    /**
     * generate last id
     *
     * @param ps
     * @return void
     */
    private void generateLastId(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            this.lastId = rs.getInt(1);
        }
    }

    /**
     * get last id
     *
     * @return int
     */
    public int getLastId() throws SQLException {
        return this.lastId;
    }

    /**
     * Add the given value to bindings
     *
     * @param value
     * @return void
     */
    private void setBindings(Object... value) {
        this.bindings.addAll(Arrays.asList(value));
    }

    /**
     * Reset All Data
     *
     * @return void
     */
    private void reset() {
        this.bindings = new ArrayList<>();
        this.data = new LinkedHashMap<String, Object>();
        this.joins = new ArrayList<>();
        this.limit = 0;
        this.offset = 0;
        this.orderBy = new ArrayList<>();
        this.selects = new ArrayList<>();
        this.sort = null;
        this.table = null;
        this.wheres = new ArrayList<>();
    }
}
