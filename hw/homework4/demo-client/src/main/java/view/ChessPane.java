package view;


import entity.FiveChess;
import enums.Side;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class ChessPane extends Pane {

    // 画布对象，用于绘制棋盘和棋子
    public Canvas canvas;

    // 图形上下文，用于在画布上进行绘图操作
    public GraphicsContext gc;

    // 五子棋游戏逻辑对象
    public FiveChess fiveChess;

    // 获取画布对象的方法
    public Canvas getCanvas() {
        return canvas;
    }

    // 获取图形上下文的方法
    public GraphicsContext getGc() {
        return gc;
    }

    // 获取五子棋游戏逻辑对象的方法
    public FiveChess getFiveChess() {
        return fiveChess;
    }

    // 设置五子棋游戏逻辑对象的方法
    public void setFiveChess(FiveChess fiveChess) {
        this.fiveChess = fiveChess;
    }

    // 构造方法，初始化五子棋面板
    public ChessPane(FiveChess fiveChess){
        this.fiveChess = fiveChess; // 初始化五子棋游戏逻辑对象
        double cell = fiveChess.getCellLen(); // 获取每个格子的边长
        canvas = new Canvas(800, 700); // 创建画布对象，并设置其大小
        gc = canvas.getGraphicsContext2D(); // 获取画布的图形上下文
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // 清空画布
        // drawPane(cell); // 调用绘制棋盘的方法（注释掉）
        drawChess(cell); // 调用绘制棋子的方法
        getChildren().add(canvas); // 将画布添加到当前面板中
    }

    /*渲染棋盘*/
    public void drawPane(double cell){
        gc.setStroke(Color.BLACK); // 设置画笔颜色为黑色

        // 遍历棋盘的每一行和每一列，绘制小方格
        for(int i = 0; i < fiveChess.getWidth(); i++)
            for(int j = 0; j < fiveChess.getHeight(); j++){
                gc.strokeRect(100 + i * cell, 100 + cell * j, cell, cell); // 画出小方格
            }
    }

    /*渲染棋子*/
    public void drawChess(double cell){
        int[][] chess = fiveChess.getChess(); // 获取棋子的位置信息

        // 遍历棋盘的每一行和每一列，绘制棋子
        for(int i = 0; i < fiveChess.getHeight(); i++)
            for(int j = 0; j < fiveChess.getWidth(); j++){
                if(chess[i][j] == Side.BLACK.getCode()){ // 如果当前位置是黑棋
                    gc.setFill(Color.BLACK); // 设置填充颜色为黑色
                    gc.fillOval(100 + i * cell - cell / 2, 100 + j * cell - cell / 2, cell, cell); // 画棋子
                } else if(chess[i][j] == Side.WHITE.getCode()){ // 如果当前位置是白棋
                    gc.setFill(Color.WHITE); // 设置填充颜色为白色
                    gc.fillOval(100 + i * cell - cell / 2, 100 + j * cell - cell / 2, cell, cell); // 画棋子
                    gc.strokeOval(100 + i * cell - cell / 2, 100 + j * cell - cell / 2, cell, cell); // 描边棋子
                }
            }
    }

    // 重置棋盘的方法，清空棋盘并重新绘制
    public void resetChessPane() {
        fiveChess.reset(); // 重置五子棋游戏逻辑对象
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // 清空画布
    }
}

