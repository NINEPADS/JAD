package entity;

import enums.Side;
import enums.State;

import java.net.Socket;
import java.sql.Date;
import java.sql.Timestamp;

public class Game {
    // 定义客户端的Socket连接，用于白方和黑方的通信
    Socket clientWhite;
    Socket clientBlack;

    // 当前轮到哪一方下棋
    Side currentSide;

    // 当前游戏的状态
    State currentState;

    // 五子棋游戏逻辑处理对象
    FiveChess fiveChess;

    // 用于存储游戏过程中的信息内容
    StringBuilder content = new StringBuilder();

    // 记录游戏开始的时间戳
    Timestamp startTime;

    // 黑方用户的ID
    int userIdBlack = 0;

    // 白方用户的ID
    int userIdWhite = 0;

    // 黑方用户名
    String usernameBlack = "";

    // 白方用户名
    String usernameWhite = "";
    public Game() {}

    public Side getCurrentSide() {
        return currentSide;
    }

    public void setCurrentSide(Side currentSide) {
        this.currentSide = currentSide;
    }

    public State getCurrentState() {
        return currentState;
    }
    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
    public Socket getClientWhite() {
        return clientWhite;
    }
    public void setClientWhite(Socket clientWhite) {
        this.clientWhite = clientWhite;
    }
    public Socket getClientBlack() {
        return clientBlack;
    }
    public void setClientBlack(Socket clientBlack) {
        this.clientBlack = clientBlack;
    }
    public void setFiveChess(FiveChess fiveChess) {
        this.fiveChess = fiveChess;
    }
    public FiveChess getFiveChess() {
        return fiveChess;
    }
    public void addContent(String content) {
        this.content.append(content);
    }
    public Timestamp getStartTime() {
        return startTime;
    }
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    public int getUserIdBlack() {
        return userIdBlack;
    }
    public void setUserIdBlack(int userIdBlack) {
        this.userIdBlack = userIdBlack;
    }
    public int getUserIdWhite() {
        return userIdWhite;
    }
    public void setUserIdWhite(int userIdWhite) {
        this.userIdWhite = userIdWhite;
    }
    public String getContent() {
        return content.toString();
    }
    public void SetUsernameBlack(String usernameBlack) {
        this.usernameBlack = usernameBlack;
    }
    public void SetUsernameWhite(String usernameWhite) {
        this.usernameWhite = usernameWhite;
    }
    public String getUsernameBlack() {
        return usernameBlack;
    }
    public String getUsernameWhite() {
        return usernameWhite;
    }
}
