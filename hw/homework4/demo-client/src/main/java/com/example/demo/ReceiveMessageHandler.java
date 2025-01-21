package com.example.demo;

import entity.ClientInfo;
import entity.History;
import enums.Side;
import enums.State;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.ChessPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ReceiveMessageHandler implements Runnable {
    Socket socket;
    BufferedReader in;
    Stage stage;
    MainHandler mainHandler;
    ReceiveMessageHandler(Socket socket, BufferedReader in, Stage stage, MainHandler mainHandler) {
        this.socket = socket;
        this.in = in;
        this.stage = stage;
        this.mainHandler = mainHandler;
    }

    @Override
    public void run() {
        // 无限循环，确保线程持续运行
        while (true) {
            // 如果socket未连接，则跳过本次循环
            if (!socket.isConnected()) {
                continue;
            }
            try {
                String response;
                // 从输入流中读取服务器的响应
                while ((response = in.readLine()) != null) {
                    // 打印服务器响应到控制台
                    System.out.println(response);
                    // 根据响应的第一个单词进行不同的操作
                    switch (response.split(" ")[0]) {
                        case "createGame", "joinGame" -> createGame(response); // 创建或加入游戏
                        case "placeChess" -> placeChess(response); // 放置棋子
                        case "quitGame" -> quitGame(); // 退出游戏
                        case "startGame" -> startGame(response); // 开始游戏
                        case "overGame" -> overGame(); // 结束游戏
                        case "login" -> login(); // 登录
                        case "clientList" -> clientList(response); // 获取客户端列表
                        case "records" -> showAllHistory(response); // 显示所有历史记录
                        case "queryRecord" -> showRecord(response); // 查询特定记录
                        case "noGame" -> noGame(); // 无游戏处理
                    }
                }
            } catch (IOException e) {
                // 捕获IO异常并打印错误信息
                System.err.println("无法连接到服务器：" + e.getMessage());
                // 显示错误提示框
                showAlert(Alert.AlertType.ERROR, "连接失败", "无法连接到服务器: " + e.getMessage());
            }
        }
    }


    @FXML
    public void createGame(String response) {
        // 将响应字符串按空格分割
        String[] split = response.split(" ");
        System.out.println("createGame"); // 打印日志信息
        mainHandler.setState(State.WAIT); // 设置主处理器状态为等待
        Platform.runLater(() -> {
            // 在JavaFX应用线程中运行代码
            mainHandler.getChessPane().drawPane(mainHandler.getChessPane().fiveChess.getCellLen()); // 绘制棋盘面板
            stage.setTitle("五子棋对局" + split[1]); // 设置窗口标题
            stage.show(); // 显示窗口
        });
    }

    public void placeChess(String response) {
        System.out.println("placeChess"); // 打印日志信息
        String[] split = response.split(" "); // 将响应字符串按空格分割

        // 根据当前棋子颜色切换到对方颜色
        if (mainHandler.getCurrentSide() == Side.WHITE) {
            mainHandler.setCurrentSide(Side.BLACK);
        } else if (mainHandler.getCurrentSide() == Side.BLACK) {
            mainHandler.setCurrentSide(Side.WHITE);
        }

        // 如果响应长度为3，则解析并下棋
        if (split.length == 3) {
            mainHandler.getChessPane().getFiveChess().play(Integer.parseInt(split[1]), Integer.parseInt(split[2])); // 执行下棋操作
            Platform.runLater(() -> {
                // 在JavaFX应用线程中运行代码
                mainHandler.getChessPane().drawChess(mainHandler.getChessPane().fiveChess.getCellLen()); // 绘制棋子
            });
        }
    }

    public void quitGame() {
        System.out.println("quitGame"); // 打印日志信息
        mainHandler.started = false; // 标记游戏未开始
        mainHandler.resetGame(); // 重置游戏状态
        Platform.runLater(() -> {
            // 在JavaFX应用线程中运行代码
            if (stage.isShowing()) {
                stage.close(); // 关闭窗口
            }
        });
        mainHandler.setState(State.RELAX); // 设置主处理器状态为放松
    }

    public void overGame() {
        System.out.println("overGame"); // 打印日志信息
        // 根据当前棋子颜色确定获胜方
        String winner = mainHandler.getCurrentSide() == Side.WHITE ? "黑方" : "白方";

        Platform.runLater(() -> {
            // 在JavaFX应用线程中运行代码
            Alert alert = new Alert(Alert.AlertType.INFORMATION); // 创建信息提示框
            alert.setTitle("对局结束"); // 设置提示框标题
            alert.setHeaderText(null); // 设置提示框头部文本为空
            alert.setContentText("获胜的是" + winner); // 设置提示框内容文本
            alert.setOnCloseRequest(event -> {
                quitGame(); // 当提示框关闭时调用退出游戏方法
            });
            alert.showAndWait(); // 显示提示框并等待用户操作
        });
        mainHandler.setState(State.WAIT); // 设置主处理器状态为等待
    }

    public void clientList(String response) {
        System.out.println("clientList"); // 打印日志信息
        String[] split = response.split(" "); // 将响应字符串按空格分割
        ObservableList<ClientInfo> observableList = FXCollections.observableArrayList(); // 创建可观察列表用于存储客户端信息
        for (int i = 1; i < split.length; i++) {
            String[] info = split[i].split(","); // 将每个客户端信息按逗号分割
            if(info.length == 3) {
                ClientInfo clientInfo = new ClientInfo(info[1], info[0], info[2]); // 创建客户端信息对象
                System.out.println(clientInfo); // 打印客户端信息
                observableList.add(clientInfo); // 将客户端信息添加到列表中
            }
        }
        Platform.runLater(() -> {
            // 在JavaFX应用线程中运行代码
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("clientlists-view.fxml")); // 加载FXML文件
                Parent root = loader.load(); // 加载根节点
                ClientListHandler clientListHandler = loader.getController(); // 获取控制器实例
                clientListHandler.setClientList(observableList); // 设置客户端列表数据
                Stage stage = new Stage(); // 创建新舞台
                Scene scene = new Scene(root); // 创建场景并设置根节点
                stage.setScene(scene); // 设置舞台场景
                stage.setTitle("在线客户端"); // 设置窗口标题
                stage.show(); // 显示窗口
            } catch (IOException e) {
                throw new RuntimeException(e); // 捕获IO异常并抛出运行时异常
            }
        });
    }

    public void login() {
        // 打印登录信息到控制台
        System.out.println("login");
        // 设置用户已登录状态
        mainHandler.setIsLogined(true);
    }

    public void startGame(String response) {
        // 打印开始游戏信息到控制台
        System.out.println("startGame");
        // 将响应字符串按空格分割成数组
        String[] split = response.split(" ");
        // 如果分割后的数组长度为2，则继续处理
        if (split.length == 2) {
            // 根据第二个元素判断玩家的棋子颜色
            if(split[1].equals("white")) {
                mainHandler.setSide(Side.WHITE); // 设置为白方
            } else if (split[1].equals("black")) {
                mainHandler.setSide(Side.BLACK); // 设置为黑方
            }
        }
        // 在JavaFX线程中运行以下代码
        Platform.runLater(() -> {
            // 显示对局开始的信息提示框
            showAlert(Alert.AlertType.INFORMATION, "对局开始", "你是" + mainHandler.getSide().getDesc());
        });
        // 设置游戏已开始状态
        mainHandler.setStarted(true);
        // 设置游戏状态为运行中
        mainHandler.setState(State.RUN);
    }

    public void showAllHistory(String response) {
        // 打印显示所有历史记录信息到控制台
        System.out.println("showAllHistory");
        // 将响应字符串按空格分割成数组
        String[] split = response.split(" ");
        // 创建一个可观察的历史记录列表
        ObservableList<History> observableList = FXCollections.observableArrayList();
        // 遍历分割后的数组，从索引1开始（忽略第一个元素）
        for (int i = 1; i < split.length; i++) {
            // 将每个元素按逗号分割成更详细的信息数组
            String[] info = split[i].split(",");
            // 如果详细信息数组长度为5，则创建历史记录对象并添加到列表中
            if(info.length == 5) {
                History history = new History(info[0], info[1], info[2], info[3].replace(".", " ").substring(0, info[3].length()-1), info[4].replace(".", " ").substring(0, info[4].length()-1));
                observableList.add(history);
            }
        }
        // 在JavaFX线程中运行以下代码
        Platform.runLater(() -> {
            // 加载历史记录视图的FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("history-view.fxml"));
            Parent root = null;
            try {
                root = loader.load(); // 加载FXML文件内容
            } catch (IOException e) {
                throw new RuntimeException(e); // 捕获并抛出IO异常
            }
            // 获取控制器实例
            HistoryHandler historyHandler = loader.getController();
            // 设置历史记录表的数据
            historyHandler.setHistoryTable(observableList);
            // 创建新舞台并设置场景和标题
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("历史对局记录");
            stage.show(); // 显示舞台
        });
    }

    public void showRecord(String response) {
        // 打印显示记录信息到控制台
        System.out.println("showRecord");
        // 将响应字符串按空格分割成数组
        String[] split = response.split(" ");
        // 使用StringBuilder构建文本内容
        StringBuilder text = new StringBuilder();
        // 遍历分割后的数组，从索引1开始（忽略第一个元素）
        for (int i = 1; i < split.length; i++) {
            text.append(split[i]); // 添加每段文本到StringBuilder中
            text.append("\n"); // 添加换行符
        }
        // 在JavaFX线程中运行以下代码
        Platform.runLater(() -> {
            // 创建一个文本区域并设置属性
            TextArea textArea = new TextArea();
            textArea.setWrapText(true); // 自动换行
            textArea.setEditable(false); // 设置为不可编辑
            // 将文本区域放入一个滚动面板中
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(textArea);
            scrollPane.setFitToWidth(true); // 设置滚动面板宽度适应内容
            scrollPane.setFitToHeight(true); // 设置滚动面板高度适应内容
            textArea.setText(text.toString()); // 设置文本区域的内容
            // 创建新舞台并设置场景和标题
            Stage popupStage = new Stage();
            popupStage.setTitle("对局记录");
            VBox vbox = new VBox(scrollPane);
            Scene scene = new Scene(vbox, 300, 200);
            popupStage.setScene(scene);
            popupStage.show(); // 显示舞台
        });
    }

    public void noGame() {
        // 在JavaFX线程中运行以下代码
        Platform.runLater(() -> {
            // 显示警告提示框，提示没有可加入的对局
            showAlert(Alert.AlertType.WARNING, "加入失败", "没有可加入的对局");
        });
    }

    // 新增方法用于显示提示框
    private void showAlert(Alert.AlertType Type, String title, String message) {
        // 创建并配置警告框
        Alert alert = new Alert(Type);
        alert.setTitle(title); // 设置标题
        alert.setHeaderText(null); // 设置头部文本为空
        alert.setContentText(message); // 设置内容文本
        alert.showAndWait(); // 显示警告框并等待用户响应
    }

}
