package com.example.demo;

import entity.FiveChess;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.ChessPane;

import java.io.*;
import java.net.Socket;

// 客户端主线程



public class ClientDemo1 extends Application {



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        primaryStage.setTitle("登录");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(new ClosePrimaryStageHandler());
        primaryStage.show();

    }
    class ClosePrimaryStageHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent event) {
            System.exit(0);
        }
    }
}
