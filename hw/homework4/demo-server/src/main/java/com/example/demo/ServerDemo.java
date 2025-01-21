package com.example.demo;

import Controller.ClientHandler;
import Controller.DatabaseManager;
import entity.ClientInfo;
import entity.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerDemo {
    private static final int PORT = 3495; // 服务器监听的端口号
    // 使用同步Map来存储客户端信息，确保线程安全
    private static final Map<String, ClientInfo> clients = Collections.synchronizedMap(new HashMap<>());
    // 存储等待中的游戏
    private static final Map<Integer, Game> waitingGames = Collections.synchronizedMap(new HashMap<>());
    // 存储正在运行的游戏
    private static final Map<Integer, Game> runningGames = Collections.synchronizedMap(new HashMap<>());
    // 存储已登录的用户
    private static final Set<String> loggedUsers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager(); // 初始化数据库管理器
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // 创建服务器套接字并绑定到指定端口
            while (true) { // 无限循环，持续接受客户端连接
                Socket clientSocket = serverSocket.accept(); // 接受客户端连接请求
                System.out.println("客户端已连接"); // 打印客户端连接信息
                // 为每个客户端连接启动一个新的线程，处理客户端请求
                new Thread(new ClientHandler(clientSocket, clients, waitingGames, runningGames, loggedUsers)).start();
            }
        } catch (IOException e) { // 捕获IO异常
            System.err.println("无法启动服务器：" + e.getMessage()); // 打印错误信息
        }
    }
}
