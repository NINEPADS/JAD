package entity;

import enums.State;
import javafx.beans.property.SimpleStringProperty;

import java.beans.JavaBean;

public class ClientInfo {

    // 声明一个私有且不可变的SimpleStringProperty类型的变量port，用于存储端口信息
    private final SimpleStringProperty port;

    // 声明一个私有且不可变的SimpleStringProperty类型的变量username，用于存储用户名信息
    private final SimpleStringProperty username;

    // 声明一个私有且不可变的SimpleStringProperty类型的变量state，用于存储客户端状态信息
    private final SimpleStringProperty state;

    public ClientInfo(String port, String username, String state) {
        this.port = new SimpleStringProperty(port);
        this.username = new SimpleStringProperty(username);
        this.state = new SimpleStringProperty(state);
    };

    public String getPort() {
        return port.get();
    }

    public void setPort(String port) {
        this.port.set(port);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public SimpleStringProperty portProperty() {
        return port;
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }
}
