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
    private int n; // 未使用的变量，可以删除或注释掉

    private double width; // 棋盘的宽度

    private double height; // 棋盘的高度

    /**
     * 小方格的大小
     */
    private double cellLen; // 小方格的大小

    /**
     * 当前棋手
     */
    private Side currentSide = Side.BLACK; // 默认当前棋手为黑棋

    // 获取当前棋子的标志字符
    public char getFlag() {
        return flag;
    }

    private char flag = ' '; // 初始化标志字符为空格

    private int[][] chess; // 存储棋盘状态的二维数组

    // 获取棋盘状态
    public int[][] getChess() {
        return chess;
    }

    // 设置棋盘状态
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

    /**
     * 构造函数，初始化棋盘大小和每个小方格的大小，并清空棋盘
     * @param width 棋盘的宽度
     * @param height 棋盘的高度
     * @param cellLen 小方格的大小
     */
    public FiveChess(double width, double height, double cellLen) {
        this.width = width;
        this.height = height;
        this.cellLen = cellLen;
        chess = new int[(int) height][(int) width]; // 初始化棋盘数组
        for (int i = 0; i < height; i++) // 遍历每一行
            for (int j = 0; j < width; j++) // 遍历每一列
                chess[i][j] = ' '; // 将每个位置设置为空格表示空位
    }

    /**
     * 在指定位置下棋，如果该位置为空则放置当前棋手的棋子，并切换到下一个棋手
     * @param x 横坐标
     * @param y 纵坐标
     */
    public void play(int x, int y) {
        // 将当前的棋子放置到（x,y）
        if (chess[x][y] == ' ') { // 如果该位置为空
            chess[x][y] = currentSide.getCode(); // 放置当前棋手的棋子
            changeSide(); // 更换下棋方
        }
    }

    /**
     * 打印当前棋盘状态
     */
    public void print() {
        for (int i = 0; i < height; i++) { // 遍历每一行
            for (int j = 0; j < width; j++) { // 遍历每一列
                System.out.print(chess[i][j]); // 打印每个位置的棋子状态
            }
            System.out.println(); // 换行
        }
    }

    /**
     * 更换下棋方
     */
    public void changeSide() {
        // 更换下棋方
        setCurrentSide(currentSide == Side.BLACK ? Side.WHITE : Side.BLACK); // 切换当前棋手
    }

    /**
     * 重置棋盘状态，将所有位置设为空，并将当前棋手设为黑棋
     */
    public void reset() {
        for (int i = 0; i < height; i++) { // 遍历每一行
            for (int j = 0; j < width; j++) { // 遍历每一列
                chess[i][j] = ' '; // 将每个位置设置为空格表示空位
            }
        }
        currentSide = Side.BLACK; // 将当前棋手设为黑棋
    }

    /**
     * 判断游戏是否结束，通过横向、纵向、主对角线和副对角线方向进行判断
     * @param row 当前棋子的行坐标
     * @param col 当前棋子的列坐标
     * @param chessColor 当前棋子的颜色
     * @return 如果游戏结束返回true，否则返回false
     */
    public boolean judgeGame(int row, int col, Side chessColor) {
        // 判断游戏是否结束
        return rowJudge(row, col, chessColor) && colJudge(row, col, chessColor) && mainDiagonalJudge(row, col, chessColor) && DeputyDiagonalJudge(row, col, chessColor);
    }

    /**
     * 横向判断五子连线
     * @param row 当前棋子的行坐标
     * @param col 当前棋子的列坐标
     * @param chessColor 当前棋子的颜色
     * @return 如果横向有五子连线返回false，否则返回true
     */
    public boolean rowJudge(int row, int col, Side chessColor) {
        int count = 0; // 计数器，记录连续相同颜色的棋子数量
        for (int j = col; j < width; j++) { // 向右检查连续相同颜色的棋子数量
            if (chess[row][j] != chessColor.getCode()) // 如果遇到不同颜色的棋子，停止计数
                break;
            count++; // 增加计数器
        }
        for (int j = col - 1; j >= 0; j--) { // 向左检查连续相同颜色的棋子数量
            if (chess[row][j] != chessColor.getCode()) // 如果遇到不同颜色的棋子，停止计数
                break;
            count++; // 增加计数器
        }
        if (count >= 5) // 如果连续相同颜色的棋子数量达到5个，返回false表示游戏结束
            return false;
        return true; // 否则返回true表示游戏继续
    }

    /**
     * 纵向判断五子连线
     * @param row 当前棋子的行坐标
     * @param col 当前棋子的列坐标
     * @param chessColor 当前棋子的颜色
     * @return 如果纵向有五子连线返回false，否则返回true
     */
    public boolean colJudge(int row, int col, Side chessColor) {
        int count = 0; // 计数器，记录连续相同颜色的棋子数量
        for (int i = row; i < height; i++) { // 向下检查连续相同颜色的棋子数量
            if (chess[i][col] != chessColor.getCode()) // 如果遇到不同颜色的棋子，停止计数
                break;
            count++; // 增加计数器
        }
        for (int i = row - 1; i >= 0; i--) { // 向上检查连续相同颜色的棋子数量
            if (chess[i][col] != chessColor.getCode()) // 如果遇到不同颜色的棋子，停止计数
                break;
            count++; // 增加计数器
        }
        return count < 5; // 如果连续相同颜色的棋子数量小于5个，返回true表示游戏继续，否则返回false表示游戏结束
    }

    /**
     * 主对角线方向判断五子连线
     * @param row 当前棋子的行坐标
     * @param col 当前棋子的列坐标
     * @param chessColor 当前棋子的颜色
     * @return 如果主对角线方向有五子连线返回false，否则返回true
     */
    public boolean mainDiagonalJudge(int row, int col, Side chessColor) {
        int count = 0; // 计数器，记录连续相同颜色的棋子数量
        for (int i = row, j = col; i < height && j < width; i++, j++) { // 向右下方检查连续相同颜色的棋子数量
            if (chess[i][j] != chessColor.getCode()) // 如果遇到不同颜色的棋子，停止计数
                break;
            count++; // 增加计数器
        }
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) { // 向左上方检查连续相同颜色的棋子数量
            if (chess[i][j] != chessColor.getCode()) // 如果遇到不同颜色的棋子，停止计数
                break;
            count++; // 增加计数器
        }
        return count < 5; // 如果连续相同颜色的棋子数量小于5个，返回true表示游戏继续，否则返回false表示游戏结束
    }

    /**
     * 副对角线方向判断五子连线
     * @param row 当前棋子的行坐标
     * @param col 当前棋子的列坐标
     * @param chessColor 当前棋子的颜色
     * @return 如果副对角线方向有五子连线返回false，否则返回true
     */
    public boolean DeputyDiagonalJudge(int row, int col, Side chessColor) {
        int count = 0; // 计数器，记录连续相同颜色的棋子数量
        for (int i = row, j = col; i >= 0 && j < width; i--, j++) { // 向右上方检查连续相同颜色的棋子数量
            if (chess[i][j] != chessColor.getCode()) // 如果遇到不同颜色的棋子，停止计数
                break;
            count++; // 增加计数器
        }
        for (int i = row + 1, j = col - 1; i < height && j >= 0; i++, j--) { // 向左下方检查连续相同颜色的棋子数量
            if (chess[i][j] != chessColor.getCode()) // 如果遇到不同颜色的棋子，停止计数
                break;
            count++; // 增加计数器
        }
        return count < 5; // 如果连续相同颜色的棋子数量小于5个，返回true表示游戏继续，否则返回false表示游戏结束
    }
}
