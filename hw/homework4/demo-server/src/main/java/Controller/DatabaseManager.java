package Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Record;

public class DatabaseManager {
    // 数据库连接URL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fiveChess";
    // 数据库用户名
    private static final String DB_USER = "root";
    // 数据库密码
    private static final String DB_PASSWORD = "123456";
    // 数据库连接对象
    private static Connection con;

    // 静态代码块，用于初始化数据库连接
    static {
        try {
            System.out.println("connecting to database..."); // 打印连接数据库信息
            // 获取数据库连接
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println(e.getMessage()); // 捕获并打印SQL异常信息
        }
        System.out.println("connected to database!"); // 打印连接成功信息
    }

    /**
     * 用户登录方法
     * @param username 用户名
     * @param password 密码
     * @return 用户ID，如果登录失败返回-1
     */
    public static Integer login(String username, String password) {
        // SQL查询语句
        String sql = "select * from user where username=? and password=?";
        System.out.println(username + " " + password); // 打印用户名和密码
        try {
            // 创建PreparedStatement对象
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username); // 设置第一个参数为用户名
            ps.setString(2, password); // 设置第二个参数为密码
            ResultSet rs = ps.executeQuery(); // 执行查询
            if(rs.next()) {
                return rs.getInt("id"); // 如果查询结果存在，返回用户ID
            } else {
                return -1; // 如果查询结果不存在，返回-1表示登录失败
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage()); // 捕获并打印SQL异常信息
            return -1; // 返回-1表示登录失败
        }
    }

    /**
     * 用户注册方法
     * @param username 用户名
     * @param password 密码
     * @return 注册是否成功，成功返回true，失败返回false
     */
    public static Boolean register(String username, String password) {
        // SQL插入语句
        String sql = "insert into user (username, password) values (?, ?)";
        System.out.println(username + " " + password); // 打印用户名和密码
        try {
            // 创建PreparedStatement对象
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username); // 设置第一个参数为用户名
            ps.setString(2, password); // 设置第二个参数为密码
            ps.executeUpdate(); // 执行更新操作
            return true; // 返回true表示注册成功
        } catch (SQLException e) {
            System.err.println(e.getMessage()); // 捕获并打印SQL异常信息
            return false; // 返回false表示注册失败
        }
    }

    /**
     * 记录游戏结果方法
     * @param black_username 黑方用户名
     * @param white_username 白方用户名
     * @param content 游戏内容
     * @param start_time 游戏开始时间
     * @param end_time 游戏结束时间
     */
    public static void gameRecord(String black_username, String white_username, String content, Timestamp start_time, Timestamp end_time) {
        System.out.println(content); // 打印游戏内容
        // SQL插入语句
        String sql = "insert into record (black_username, white_username, content, start_time, end_time) values(?, ?, ?, ?, ?)";
        try {
            // 创建PreparedStatement对象
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, black_username); // 设置第一个参数为黑方用户名
            ps.setString(2, white_username); // 设置第二个参数为白方用户名
            ps.setString(3, content); // 设置第三个参数为游戏内容
            ps.setTimestamp(4, start_time); // 设置第四个参数为游戏开始时间
            ps.setTimestamp(5, end_time); // 设置第五个参数为游戏结束时间
            ps.executeUpdate(); // 执行更新操作
        } catch (SQLException e) {
            System.err.println(e.getMessage()); // 捕获并打印SQL异常信息
        }
    }

    /**
     * 查询所有游戏记录方法
     * @param user_name 用户名
     * @return 游戏记录列表
     */
    public static List<Record> queryAllRecords(String user_name) {
        // SQL查询语句
        String sql = "select * from record where black_username=? or white_username=?";
        List<Record> records = new ArrayList<Record>(); // 创建记录列表
        try {
            // 创建PreparedStatement对象
            PreparedStatement ps;
            ps = con.prepareStatement(sql);
            ps.setString(1, user_name); // 设置第一个参数为用户名
            ps.setString(2, user_name); // 设置第二个参数为用户名
            ResultSet rs = ps.executeQuery(); // 执行查询
            while(rs.next()) {
                int id = rs.getInt("id"); // 获取记录ID
                String black_username = rs.getString("black_username"); // 获取黑方用户名
                String white_username = rs.getString("white_username"); // 获取白方用户名
                Timestamp start_time = rs.getTimestamp("start_time"); // 获取游戏开始时间
                Timestamp end_time = rs.getTimestamp("end_time"); // 获取游戏结束时间
                // 创建Record对象并添加到记录列表中
                Record record = new Record(id, white_username, black_username, start_time, end_time);
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage()); // 捕获并打印SQL异常信息
        }
        return records; // 返回记录列表
    }

    /**
     * 根据ID查询游戏记录方法
     * @param id 记录ID
     * @return 游戏内容，如果查询失败返回空字符串
     */
    public static String queryRecord(int id) {
        // SQL查询语句
        String sql = "select * from record where id=?";
        try {
            // 创建PreparedStatement对象
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id); // 设置参数为记录ID
            ResultSet rs = ps.executeQuery(); // 执行查询
            if(rs.next()) {
                return rs.getString("content"); // 如果查询结果存在，返回游戏内容
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage()); // 捕获并打印SQL异常信息
        }
        return ""; // 如果查询失败，返回空字符串
    }
}
