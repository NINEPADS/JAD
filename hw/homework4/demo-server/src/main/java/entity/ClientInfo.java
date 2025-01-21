package entity;

import enums.Side;
import enums.State;

import java.net.Socket;

public class ClientInfo {
    // 用户ID，用于唯一标识用户
    private int userId;
    // 用户名，用于显示或识别用户
    private String username;
    // Socket对象，用于网络通信
    private Socket socket;
    // 用户状态，表示用户的当前状态
    private State state;
    // 游戏对象，表示用户参与的游戏
    private Game game;
    // 登录状态，表示用户是否已登录
    private boolean logged;
    // 用户所在方，表示用户在游戏中的位置或阵营
    private Side side;

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public boolean isLogged() {
        return logged;
    }
    public void setLogged(boolean logged) {
        this.logged = logged;
    }
    public Side getSide() {
        return side;
    }
    public void setSide(Side side) {
        this.side = side;
    }
}
