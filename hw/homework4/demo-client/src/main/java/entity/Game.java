package entity;

import enums.Side;
import enums.State;

import java.net.Socket;

public class Game {
    // 声明一个Socket对象，用于表示白方客户端连接
    Socket clientWhite;
    // 声明一个Socket对象，用于表示黑方客户端连接
    Socket clientBlack;
    // 声明一个Side类型的变量，用于记录当前轮到哪一方下棋
    Side currentSide;
    // 声明一个State类型的变量，用于记录游戏当前的状态
    State currentState;
    // 声明一个FiveChess类型的变量，用于管理五子棋的游戏逻辑
    FiveChess fiveChess;
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
}
