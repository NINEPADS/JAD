package com.example.demo;

import entity.FiveChess;
import enums.Side;
import enums.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.ChessPane;
import javafx.scene.control.Alert; // 新增导入
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainHandler {
    // 客户端与服务端通信的Socket
    Socket socket;
    // 客户端通过out向服务端发送信息
    PrintWriter out;
    // 客户端的持黑或持白
    Side side;
    // 当前轮到黑或白的回合
    Side currentSide = Side.BLACK;
    // 对局是否开始
    Boolean started = false;
    // 下棋的棋盘
    FiveChess fiveChess;
    ChessPane chessPane;
    // 客户端当前状态
    State state = State.RELAX;
    // 客户端是否登录
    Boolean isLogined = false;

    @FXML
    public void initialize() throws IOException {
        socket = ConnectionManager.getSocket(); // 获取连接管理器中的Socket对象
        out = ConnectionManager.getOut(); // 获取连接管理器中的PrintWriter对象
        fiveChess = new FiveChess(20, 20, 28); // 初始化五子棋棋盘，大小为20x20，网格间距为28
        chessPane = new ChessPane(fiveChess); // 创建棋盘面板
        chessPane.setOnMouseClicked(new PlayActionHandler(fiveChess)); // 设置鼠标点击事件处理器
        // 我们创建一个消息处理进程帮助接受并处理服务端的消息
        // 因为这里只有服务端与我们通信，所以没有设计消息队列
        Scene scene = new Scene(chessPane); // 创建场景并添加棋盘面板
        Stage stage = new Stage(); // 创建舞台
        stage.setScene(scene); // 将场景添加到舞台
        stage.setOnCloseRequest(new CloseChessPaneHandler()); // 设置关闭窗口事件处理器
        Thread recvThread = new Thread(new ReceiveMessageHandler(socket, ConnectionManager.getIn(), stage, this)); // 创建接收消息线程
        recvThread.start(); // 启动接收消息线程
    }

    class CloseChessPaneHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent event) {
            quitGame(); // 当窗口关闭时退出游戏
        }
    }

    class PlayActionHandler implements EventHandler<MouseEvent> {
        FiveChess fiveChess;
        PlayActionHandler(FiveChess fiveChess) {
            this.fiveChess = fiveChess; // 初始化五子棋对象
        }
        @Override
        public void handle(MouseEvent event) {
            double cell = fiveChess.getCellLen(); // 获取每个格子的长度
            // 获取鼠标点击坐标
            double x = event.getX();
            double y = event.getY();

            /*映射到数组中的坐标*/
            int i = (int)((x-100+cell/2)/cell); // 计算点击位置对应的行索引
            int j = (int)((y-100+cell/2)/cell); // 计算点击位置对应的列索引
            if(i < 0 || j < 0 || i > 19 || j > 19) { // 如果点击位置超出棋盘范围，则返回
                return;
            }
            placeChess(i, j); // 放置棋子
        }
    }

    @FXML
    protected void createGame(ActionEvent event) {
        if (!isLogined) { // 如果未登录，显示警告提示框
            showAlert(AlertType.WARNING, "注意", "还未登录");
            return;
        }
        if (state != State.RELAX) { // 如果已经在对局中，显示警告提示框
            showAlert(AlertType.WARNING, "注意", "已经在对局中");
            return;
        }
        out.println("createGame"); // 向服务器发送创建游戏请求
    }

    @FXML
    protected void joinGame(ActionEvent event) {
        if (!isLogined) { // 如果未登录，显示警告提示框
            showAlert(AlertType.WARNING, "注意", "还未登录");
            return;
        }
        if (state != State.RELAX) { // 如果已经在对局中，显示警告提示框
            showAlert(AlertType.WARNING, "注意", "已经在对局中");
            return;
        }
        out.println("joinGame"); // 向服务器发送加入游戏请求
    }

    protected void quitGame() {
        out.println("quitGame"); // 向服务器发送退出游戏请求
    }

    @FXML
    protected void queryClientList(ActionEvent event) {
        System.out.println("点击了列出所有客户端按钮！"); // 打印调试信息
        System.out.println("成功连接到服务器！"); // 打印调试信息
        out.println("clientList"); // 向服务器发送“list”命令请求客户端列表
    }

    protected void placeChess(int x, int y) {
        System.out.println("start: " + started); // 打印调试信息
        if (state != State.RUN) { // 如果对局尚未开始，显示警告提示框
            showAlert(AlertType.WARNING, "注意", "对局尚未开始");
        } else if (side != currentSide) { // 如果现在不是自己的回合，显示警告提示框
            showAlert(AlertType.WARNING, "注意", "现在不是你的回合");
        } else {
            out.println("placeChess " + x + " " + y + " " + currentSide.getDesc()); // 向服务器发送放置棋子请求
        }
    }

    @FXML
    protected void queryAllHistory() {
        out.println("queryAllHistory"); // 向服务器发送查询所有历史记录请求
    }

    // 新增方法用于显示提示框
    private void showAlert(AlertType Type, String title, String message) {
        Alert alert = new Alert(Type); // 创建提示框对象
        alert.setTitle(title); // 设置提示框标题
        alert.setHeaderText(null); // 设置提示框头部文本为空
        alert.setContentText(message); // 设置提示框内容文本
        alert.showAndWait(); // 显示提示框并等待用户响应
    }

    public void setStarted (Boolean started) {
        this.started = started; // 设置对局是否开始的状态
    }

    public void setCurrentSide (Side currentSide) {
        this.currentSide = currentSide; // 设置当前轮到哪一方下棋
    }

    public Side getCurrentSide () {
        return currentSide; // 获取当前轮到哪一方下棋
    }

    public Side getSide () {
        return side; // 获取当前玩家执子的一方（黑或白）
    }

    public void setSide (Side side) {
        this.side = side; // 设置当前玩家执子的一方（黑或白）
    }

    public ChessPane getChessPane() {
        return chessPane; // 获取棋盘面板对象
    }

    public void setState (State state) {
        this.state = state; // 设置当前客户端状态
    }

    public void setIsLogined (boolean isLogined) {
        this.isLogined = isLogined; // 设置当前客户端是否已登录状态
    }

    public void resetGame() {
        currentSide = Side.BLACK; // 重置当前轮到哪一方下棋为黑方
        chessPane.resetChessPane(); // 重置棋盘面板
    }

    @FXML
    public void quit(ActionEvent event) {
        if(state != State.RUN) { // 如果对局未开始，直接退出程序
            try {
                socket.close(); // 关闭Socket连接
                System.exit(0); // 退出程序
            } catch (IOException e) {
                System.err.println(e.getMessage()); // 打印异常信息
            }
        } else { // 如果对局已经开始，先退出游戏再退出程序
            quitGame(); // 退出游戏
            try {
                socket.close(); // 关闭Socket连接
                System.exit(0); // 退出程序
            } catch (IOException e) {
                System.err.println(e.getMessage()); // 打印异常信息
            }
        }
    }
}
