package entity;


import enums.Side;


public class FiveChess {

    // 获取棋盘宽度
    public double getWidth() {
        return width;
    }

    // 设置棋盘宽度
    public void setWidth(double width) {
        this.width = width;
    }

    // 获取棋盘高度
    public double getHeight() {
        return height;
    }

    // 设置棋盘高度
    public void setHeight(double height) {
        this.height = height;
    }

    // 获取小方格的大小
    public double getCellLen() {
        return cellLen;
    }

    // 设置小方格的大小
    public void setCellLen(double cellLen) {
        this.cellLen = cellLen;
    }

    /**
     * 维度
     */
    private int n; // 未使用的变量，可能是多余的

    private double width; // 棋盘的宽度

    private double height; // 棋盘的高度

    /**
     * 小方格的大小
     */
    private double cellLen; // 每个小方格的长度

    /**
     * 当前棋手
     */
    private Side currentSide = Side.BLACK; // 默认当前棋手为黑棋

    // 获取当前标志（未使用）
    public char getFlag() {
        return flag;
    }

    private char flag = ' '; // 未使用的标志变量，可能是多余的

    private int[][] chess; // 棋盘数组，存储棋子信息

    // 获取棋盘数组
    public int[][] getChess() {
        return chess;
    }

    // 设置棋盘数组
    public void setChess(int[][] chess) {
        this.chess = chess;
    }

    // 获取当前棋手
    public Side getCurrentSide() {
        return currentSide;
    }

    // 设置当前棋手
    public void setCurrentSide(Side currentSide) {
        this.currentSide = currentSide;
    }

    // 构造函数，初始化棋盘大小和棋子位置
    public FiveChess(double width, double height, double cellLen) {
        this.width = width; // 设置棋盘宽度
        this.height = height; // 设置棋盘高度
        this.cellLen = cellLen; // 设置小方格大小
        chess = new int[(int) height][(int) width]; // 初始化棋盘数组
        for (int i = 0; i < height; i++) // 遍历每一行
            for (int j = 0; j < width; j++) // 遍历每一列
                chess[i][j] = ' '; // 将每个格子初始化为空
    }

    // 在指定位置下棋
    public void play(int x, int y) {
        // 如果该位置没有棋子，则放置当前棋手的棋子并更换棋手
        if (chess[x][y] == ' ') {
            chess[x][y] = currentSide.getCode(); // 放置棋子
            changeSide(); // 更换棋手
        }
    }

    // 更换下棋方
    public void changeSide() {
        // 如果当前是黑棋，则切换为白棋；否则切换为黑棋
        setCurrentSide(currentSide == Side.BLACK ? Side.WHITE : Side.BLACK);
    }

    // 重置棋盘，将所有位置清空并设置当前棋手为黑棋
    public void reset() {
        for (int i = 0; i < height; i++) { // 遍历每一行
            for (int j = 0; j < width; j++) { // 遍历每一列
                chess[i][j] = ' '; // 将每个格子重置为空
            }
        }
        currentSide = Side.BLACK; // 重置当前棋手为黑棋
    }
}
