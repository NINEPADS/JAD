package com.example.demo;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginHandler {
    @FXML
    private TextField usernameField; // 用户名输入框
    @FXML private PasswordField passwordField; // 密码输入框

    // 登录按钮绑定方法
    @FXML
    private void handleLogin() {
        String username = usernameField.getText(); // 获取用户名
        String password = passwordField.getText(); // 获取密码
        // 向服务端发送登录请求
        // 如果服务端回应login说明登录成功
        try {
            Socket socket = ConnectionManager.getSocket(); // 获取连接的socket
            PrintWriter out = ConnectionManager.getOut(); // 获取输出流
            BufferedReader in = ConnectionManager.getIn(); // 获取输入流
            out.println("login " + username + " " + password); // 发送登录信息到服务器
            String response = in.readLine(); // 读取服务器响应
            if (response.equals("login")) { // 判断是否登录成功
                ConnectionManager.setLogined(true); // 设置登录状态为已登录
                Platform.runLater(() -> { // 在JavaFX线程中更新UI
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-page.fxml")); // 加载主页面布局
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), 800, 600); // 创建新的场景
                    } catch (IOException e) {
                        throw new RuntimeException(e); // 处理异常
                    }
                    Stage stage = new Stage(); // 创建新的舞台
                    stage.setScene(scene); // 设置场景
                    stage.setTitle("五子棋游戏 Demo0"); // 设置标题
                    stage.setOnCloseRequest(new CloseStageHandler()); // 设置关闭事件处理器
                    stage.show(); // 显示新舞台
                    Stage stage1 = (Stage) usernameField.getScene().getWindow(); // 获取当前窗口
                    stage1.close(); // 关闭当前窗口
                    //showAlert(Alert.AlertType.INFORMATION, "登录成功", "");
                });
            } else { // 处理登录失败的情况
                String[] split = response.split(" "); // 分割响应字符串
                if (split.length == 2) { // 确保响应格式正确
                    switch (split[1]) { // 根据错误类型显示不同的提示信息
                        case "logged" -> showAlert(Alert.AlertType.WARNING, "登录失败", "该用户已经登录!");
                        case "wrong" -> showAlert(Alert.AlertType.WARNING, "登录失败", "密码或用户名错误!");
                        default -> showAlert(Alert.AlertType.ERROR, "登录失败", "未知错误!");
                    }
                }
            }
        } catch (IOException e) { // 捕获IO异常
            System.err.println(e.getMessage()); // 打印错误信息
        }
        System.out.println("Username: " + username + ", Password: " + password); // 打印用户名和密码（调试用）
    }

    // 注册按钮绑定方法
    @FXML
    private void handleRegister() throws Exception {
        Stage stage = (Stage) usernameField.getScene().getWindow(); // 获取当前窗口
        Parent root = FXMLLoader.load(getClass().getResource("register-view.fxml")); // 加载注册页面
        Scene scene = new Scene(root); // 创建新的场景
        stage.setScene(scene); // 设置新的场景
        stage.setTitle("注册"); // 设置标题
        stage.show(); // 显示新的场景
    }

    // 新增方法用于显示提示框
    private void showAlert(Alert.AlertType Type, String title, String message) {
        Alert alert = new Alert(Type); // 创建提示框对象
        alert.setTitle(title); // 设置标题
        alert.setHeaderText(null); // 设置头部文本为空
        alert.setContentText(message); // 设置内容文本
        alert.showAndWait(); // 显示提示框并等待用户响应
    }

    class CloseStageHandler implements EventHandler<WindowEvent> { // 定义关闭事件处理器类
        @Override
        public void handle(WindowEvent event) { // 实现handle方法处理关闭事件
            System.exit(0); // 退出程序
        }
    }
}
