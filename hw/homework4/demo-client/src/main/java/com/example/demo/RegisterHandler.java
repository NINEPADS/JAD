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

public class RegisterHandler {
    @FXML private TextField usernameField; // 用户名输入框
    @FXML private PasswordField passwordField; // 密码输入框

    // 处理注册按钮点击事件的方法
    @FXML
    private void handleRegister() {
        String username = usernameField.getText(); // 获取用户名输入框中的文本
        String password = passwordField.getText(); // 获取密码输入框中的文本
        if (username.isEmpty() || password.isEmpty()) { // 检查用户名或密码是否为空
            Alert alert = new Alert(Alert.AlertType.WARNING); // 创建警告类型的提示框
            alert.setTitle("注册失败"); // 设置提示框标题
            alert.setHeaderText(null); // 设置提示框头部文本为空
            alert.setContentText("请填完注册信息"); // 设置提示框内容文本
            alert.showAndWait(); // 显示提示框并等待用户响应
            return; // 结束方法执行
        }
        try {
            Socket socket = ConnectionManager.getSocket(); // 获取连接管理器的套接字
            PrintWriter out = ConnectionManager.getOut(); // 获取输出流
            BufferedReader in = ConnectionManager.getIn(); // 获取输入流
            out.println("register " + username + " " + password); // 发送注册请求到服务器
            String response = in.readLine(); // 读取服务器响应
            if (response.equals("register")) { // 如果服务器返回"register"，表示注册成功
                Platform.runLater(() -> { // 在JavaFX应用程序线程中运行代码
                    showAlert(Alert.AlertType.INFORMATION, "注册成功", "请登录"); // 显示注册成功的提示框
                    Stage stage = (Stage) usernameField.getScene().getWindow(); // 获取当前窗口的舞台
                    Parent root = null; // 初始化根节点
                    try {
                        root = FXMLLoader.load(getClass().getResource("login-view.fxml")); // 加载登录页面的FXML文件
                    } catch (IOException e) {
                        throw new RuntimeException(e); // 捕获IO异常并抛出运行时异常
                    }
                    Scene scene = new Scene(root); // 创建新的场景
                    stage.setScene(scene); // 设置新的场景
                    stage.show(); // 显示新的场景
                });
            } else {
                if (response.equals("registered")) { // 如果服务器返回"registered"，表示用户已注册
                    showAlert(Alert.AlertType.WARNING, "注册失败", "该用户已经注册!"); // 显示用户已注册的警告提示框
                } else {
                    showAlert(Alert.AlertType.ERROR, "注册失败", "未知错误"); // 显示未知错误的提示框
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage()); // 打印IO异常的错误信息
        }
        System.out.println("Username: " + username + ", Password: " + password); // 打印用户名和密码（调试用）
    }

    // 新增方法用于显示提示框
    private void showAlert(Alert.AlertType Type, String title, String message) {
        Alert alert = new Alert(Type); // 创建指定类型的提示框
        alert.setTitle(title); // 设置提示框标题
        alert.setHeaderText(null); // 设置提示框头部文本为空
        alert.setContentText(message); // 设置提示框内容文本
        alert.showAndWait(); // 显示提示框并等待用户响应
    }

    // 内部类用于处理窗口关闭事件
    class CloseStageHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent event) {
            System.exit(0); // 退出程序
        }
    }
}
