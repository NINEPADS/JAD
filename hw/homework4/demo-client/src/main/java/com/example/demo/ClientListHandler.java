package com.example.demo;

import entity.ClientInfo;
import enums.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.PrintWriter;

// 定义一个处理客户端列表的类
public class ClientListHandler {
    // 使用@FXML注解将FXML文件中的TableView组件注入到JavaFX应用程序中
    @FXML
    private TableView<ClientInfo> clientList; // 用于显示客户端信息的表格视图
    // 使用@FXML注解将FXML文件中的TableColumn组件注入到JavaFX应用程序中
    @FXML
    private TableColumn<ClientInfo, String> userNameColumn; // 用户名列
    @FXML
    private TableColumn<ClientInfo, String> portColumn; // 端口号列
    @FXML
    private TableColumn<ClientInfo, String> stateColumn; // 状态列

    /**
     * 设置客户端列表数据的方法
     * @param clients 包含客户端信息的ObservableList对象
     */
    public void setClientList(ObservableList<ClientInfo> clients) {
        // 设置userNameColumn列的单元格值工厂，从ClientInfo对象的usernameProperty属性获取值
        userNameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        // 设置portColumn列的单元格值工厂，从ClientInfo对象的portProperty属性获取值
        portColumn.setCellValueFactory(cellData -> cellData.getValue().portProperty());

        // 设置stateColumn列的单元格值工厂，从ClientInfo对象的stateProperty属性获取值
        stateColumn.setCellValueFactory(cellData -> cellData.getValue().stateProperty());

        // 将传入的客户端信息列表设置为clientList表格视图的数据源
        clientList.setItems(clients);
    }
}
