package com.example.demo;

import java.io.*;
import java.net.Socket;

public class ConnectionManager {
    // 静态变量，用于存储与服务器的连接
    private static Socket socket;
    // 静态变量，表示用户是否已登录
    private static boolean logined = false;
    // 静态变量，用于向服务器发送数据
    private static PrintWriter out;
    // 静态变量，用于从服务器接收数据
    private static BufferedReader in;

    // 静态代码块，在类加载时执行，用于初始化连接
    static {
        try {
            String serverAddress = "127.0.0.1"; // 服务器地址
            int port = 3495; // 服务器端口号
            socket = new Socket(serverAddress, port); // 创建到服务器的连接
            out = new PrintWriter(socket.getOutputStream(), true); // 初始化输出流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 初始化输入流
        } catch (IOException e) {
            System.out.println("Failed to connect to the server."); // 连接失败时打印错误信息
        }
    }

    /**
     * 获取当前与服务器的连接
     * @return 当前的Socket对象
     */
    public static Socket getSocket() {
        return socket;
    }

    /**
     * 检查用户是否已登录
     * @return 如果用户已登录返回true，否则返回false
     */
    public static boolean isLogined() {
        return logined;
    }

    /**
     * 设置用户的登录状态
     * @param logined 新的登录状态
     */
    public static void setLogined(boolean logined) {
        ConnectionManager.logined = logined;
    }

    /**
     * 获取用于向服务器发送数据的PrintWriter对象
     * @return 当前的PrintWriter对象
     */
    public static PrintWriter getOut() {
        return out;
    }

    /**
     * 获取用于从服务器接收数据的BufferedReader对象
     * @return 当前的BufferedReader对象
     */
    public static BufferedReader getIn() {
        return in;
    }
}
