package com.example.demo;

import entity.History;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.PrintWriter;

public class HistoryHandler {
    // 定义一个TableView来显示历史记录列表
    @FXML
    private TableView<History> historyList;

    // 定义表格列，用于显示白方用户名
    @FXML
    private TableColumn<History, String> whiteUsername;

    // 定义表格列，用于显示黑方用户名
    @FXML
    private TableColumn<History, String> blackUsername;

    // 定义表格列，用于显示记录ID
    @FXML
    private TableColumn<History, String> recordId;

    // 定义表格列，用于显示开始时间
    @FXML
    private TableColumn<History, String> startTime;

    // 定义表格列，用于显示结束时间
    @FXML
    private TableColumn<History, String> endTime;

    /**
     * 设置历史记录表的数据和事件处理
     * @param histories 包含历史记录的ObservableList
     */
    public void setHistoryTable(ObservableList<History> histories) {
        // 设置白方用户名列的值工厂
        whiteUsername.setCellValueFactory(cellData -> cellData.getValue().whiteUserNameProperty());

        // 设置黑方用户名列的值工厂
        blackUsername.setCellValueFactory(cellData -> cellData.getValue().blackUserNameProperty());

        // 设置记录ID列的值工厂
        recordId.setCellValueFactory(cellData -> cellData.getValue().recordIdProperty());

        // 设置开始时间列的值工厂
        startTime.setCellValueFactory(cellData -> cellData.getValue().startTimeProperty());

        // 设置结束时间列的值工厂
        endTime.setCellValueFactory(cellData -> cellData.getValue().endTimeProperty());

        // 将历史记录列表设置为TableView的项目
        historyList.setItems(histories);

        // 为historyList添加鼠标点击事件过滤器
        historyList.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            // 如果鼠标左键双击
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // 获取选中的历史记录项
                History selectedItem = historyList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // 获取输出流并发送记录请求
                    PrintWriter out = ConnectionManager.getOut();
                    out.println("record " + selectedItem.getRecordId());
                }
            }
        });
    }
}

