final class InmutableMatrix {
    // 成员变量
    private final int row;
    private final int col;
    private final int[][] matrixValues;

    /**
     * 构造函数，初始化行、列和矩阵值
     * @param row 矩阵的行数
     * @param col 矩阵的列数
     */
    public InmutableMatrix(int row, int col) {
        this.row = row;
        this.col = col;
        this.matrixValues = new int[row][col];
    }

    /**
     * 构造函数，从另一个矩阵复制行、列和矩阵值
     * @param row 矩阵的行数
     * @param col 矩阵的列数
     * @param otherMatrixValues 另一个矩阵的值
     */
    public InmutableMatrix(int row, int col, int[][] otherMatrixValues) {
        this.row = row;
        this.col = col;
        this.matrixValues = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.matrixValues[i][j] = otherMatrixValues[i][j];
            }
        }
    }

    /**
     * 构造函数，从另一个不可变矩阵复制行、列和矩阵值
     * @param others 另一个不可变矩阵
     */
    public InmutableMatrix(InmutableMatrix others) {
        this.row = others.row;
        this.col = others.col;
        this.matrixValues = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.matrixValues[i][j] = others.matrixValues[i][j];
            }
        }
    }

    /**
     * 构造函数，从可变矩阵复制行、列和矩阵值
     * @param others 一个可变矩阵
     */
    public InmutableMatrix(MutableMatrix others) {
        this.row = others.row();
        this.col = others.col();
        this.matrixValues = others.matrixValues();
    }

    /**
     * 获取矩阵的行数
     * @return 矩阵的行数
     */
    public int row() {
        return row;
    }

    /**
     * 获取矩阵的列数
     * @return 矩阵的列数
     */
    public int col() {
        return col;
    }

    /**
     * 获取矩阵的值
     * @return 矩阵的值
     */
    public int[][] matrixValues() {
        int[][] otherMatrixValues = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                otherMatrixValues[i][j] = matrixValues[i][j];
            }
        }
        return otherMatrixValues;
    }

    /**
     * 将两个不可变矩阵相加
     * @param others 另一个不可变矩阵
     * @return 相加后的不可变矩阵
     * @throws Exception 如果两个矩阵的行数或列数不同，抛出异常
     */
    public InmutableMatrix add(InmutableMatrix others) throws Exception {
        // check row and col
        if (row != others.row || col != others.col) {
            throw new Exception("The size of both mutrix should be identical!");
        }
        int[][] retValues = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                retValues[i][j] = matrixValues[i][j] + others.matrixValues[i][j];
            }
        }
        return new InmutableMatrix(row, col, retValues);
    }

    /**
     * 将两个不可变矩阵相减
     * @param others 另一个不可变矩阵
     * @return 相减后的不可变矩阵
     * @throws Exception 如果两个矩阵的行数或列数不同，抛出异常
     */
    public InmutableMatrix sub(InmutableMatrix others) throws Exception {
        // check row and col
        if (row != others.row || col != others.col) {
            throw new Exception("The size of both mutrix should be identical!");
        }
        int[][] retValues = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                retValues[i][j] = matrixValues[i][j] - others.matrixValues[i][j];
            }
        }
        return new InmutableMatrix(row, col, retValues);
    }

    /**
     * 将不可变矩阵的每个元素乘以一个标量值
     * @param number 标量值
     * @return 乘以标量值后的不可变矩阵
     */
    public InmutableMatrix scalarMultiply(int number) {
        int[][] retValues = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                retValues[i][j] = matrixValues[i][j] * number;
            }
        }
        return new InmutableMatrix(row, col, retValues);
    }

    /**
     * 将两个不可变矩阵相乘
     * @param others 另一个不可变矩阵
     * @return 相乘后的不可变矩阵
     * @throws Exception 如果第一个矩阵的列数不等于第二个矩阵的行数，抛出异常
     */
    public InmutableMatrix multiply(InmutableMatrix others) throws Exception {
        // check row and col
        if (col != others.row) {
            throw new Exception("the column of the first matrix should be the same as the row of the second!");
        }
        int[][] retValues = new int[row][others.col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < others.col; j++) {
                for (int k = 0; k < col; k++) {
                    retValues[i][j] += matrixValues[i][k] * others.matrixValues[k][j];
                }
            }
        }
        return new InmutableMatrix(row, others.col, retValues);
    }

    /**
     * 打印矩阵的值
     */
    public void print() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(matrixValues[i][j] + " ");
            }
            System.out.println();
        }
    }
}

class MutableMatrix {
    // 成员变量
    private int row;
    private int col;
    private int[][] matrixValues;

    /**
     * 构造函数，初始化行、列和矩阵值
     * @param row 矩阵的行数
     * @param col 矩阵的列数
     */
    public MutableMatrix(int row, int col) {
        this.row = row;
        this.col = col;
        this.matrixValues = new int[row][col];
    }

    /**
     * 构造函数，从另一个矩阵复制行、列和矩阵值
     * @param row 矩阵的行数
     * @param col 矩阵的列数
     * @param otherMatrixValues 另一个矩阵的值
     */
    public MutableMatrix(int row, int col, int[][] otherMatrixValues) {
        this.row = row;
        this.col = col;
        this.matrixValues = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.matrixValues[i][j] = otherMatrixValues[i][j];
            }
        }
    }

    /**
     * 构造函数，从另一个可变矩阵复制行、列和矩阵值
     * @param others 另一个可变矩阵
     */
    public MutableMatrix(MutableMatrix others) {
        this.row = others.row;
        this.col = others.col;
        this.matrixValues = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.matrixValues[i][j] = others.matrixValues[i][j];
            }
        }
    }

    /**
     * 构造函数，从不可变矩阵复制行、列和矩阵值
     * @param others 一个不可变矩阵
     */
    public MutableMatrix(InmutableMatrix others) {
        row = others.row();
        col = others.col();
        matrixValues = others.matrixValues();
    }

    /**
     * 获取矩阵的行数
     * @return 矩阵的行数
     */
    public int row() {
        return row;
    }

    /**
     * 获取矩阵的列数
     * @return 矩阵的列数
     */
    public int col() {
        return col;
    }

    /**
     * 获取矩阵的值
     * @return 矩阵的值
     */
    public int[][] matrixValues() {
        return matrixValues;
    }

    /**
     * 将两个可变矩阵相加，并更新当前矩阵的值
     * @param others 另一个可变矩阵
     * @return 更新后的可变矩阵对象本身（this）
     * @throws Exception 如果两个矩阵的行数或列数不同，抛出异常
     */
    public MutableMatrix add(MutableMatrix others) throws Exception {
        // check row and col
        if (row != others.row || col != others.col) {
            throw new Exception("The size of both mutrix should be identical!");
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrixValues[i][j] += others.matrixValues[i][j];
            }
        }
        return this;
    }

    /**
     * 将两个可变矩阵相减，并更新当前矩阵的值
     * @param others 另一个可变矩阵
     * @return 更新后的可变矩阵对象本身（this）
     * @throws Exception 如果两个矩阵的行数或列数不同，抛出异常
     */
    public MutableMatrix sub(MutableMatrix others) throws Exception {
        // check row and col
        if (row != others.row || col != others.col) {
            throw new Exception("The size of both mutrix should be identical!");
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrixValues[i][j] -= others.matrixValues[i][j];
            }
        }
        return this;
    }

    /**
     * 将可变矩阵的每个元素乘以一个标量值，并更新当前矩阵的值
     * @param number 标量值
     * @return 更新后的可变矩阵对象本身（this）
     */
    public MutableMatrix scalarMultiply(int number) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrixValues[i][j] *= number;
            }
        }
        return this;
    }
    /**
     * 将两个可变矩阵相乘，并更新当前矩阵的值
     * @param others 另一个可变矩阵
     * @return 更新后的可变矩阵对象本身（this）
     * @throws Exception 如果第一个矩阵的列数不等于第二个矩阵的行数，抛出异常
     */
    public MutableMatrix multiply(MutableMatrix others) throws Exception {
        // check row and col
        if (col != others.row) {
            throw new Exception("the column of the first matrix should be the same as the row of the second!");
        }
        int[][] retValues = new int[row][others.col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < others.col; j++) {
                for (int k = 0; k < col; k++) {
                    retValues[i][j] += matrixValues[i][k] * others.matrixValues[k][j];
                }
            }
        }
        this.matrixValues = retValues;
        this.col = others.col;
        return this;
    }
    /**
     * 打印矩阵的值
     */
    public void print() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(matrixValues[i][j] + " ");
            }
            System.out.println();
        }
    }
}

public class Test {
    static int size = 20;
    static int randomValue = 2; // 用于生成随机矩阵值
    public static void main(String[] argv) throws Exception {
        
        // 功能测试
        // 初始化样例
        int[][] valuea = {{1, 2, 3}, {4, 5, 6}};
        int[][] valueb = {{7, 8, 9}, {10, 11, 12}};
        int[][] valuec = {{1, 2}, {3, 4}, {5, 6}};

        InmutableMatrix inmutableMatrixA;
        InmutableMatrix inmutableMatrixB;
        InmutableMatrix inmutableMatrixRes;

        MutableMatrix mutableMatrixA;
        MutableMatrix mutableMatrixB;
        // 测试矩阵加法
        inmutableMatrixA = new InmutableMatrix(2, 3, valuea);
        inmutableMatrixB = new InmutableMatrix(2, 3, valueb);
        inmutableMatrixRes = inmutableMatrixA.add(inmutableMatrixB);

        mutableMatrixA = new MutableMatrix(2, 3, valuea);
        mutableMatrixB = new MutableMatrix(2, 3, valueb);
        mutableMatrixA.add(mutableMatrixB);

        /*
         * 预期结果
         * 8 10 12
         * 14 16 18
         */
        System.out.println("InmutableMatrix Add Result:");
        inmutableMatrixRes.print();
        System.out.println("MutableMatrix Add Result:");
        mutableMatrixA.print();

        // 测试矩阵减法
        inmutableMatrixA = new InmutableMatrix(2, 3, valuea);
        inmutableMatrixB = new InmutableMatrix(2, 3, valueb);
        inmutableMatrixRes = inmutableMatrixA.sub(inmutableMatrixB);

        mutableMatrixA = new MutableMatrix(2, 3, valuea);
        mutableMatrixB = new MutableMatrix(2, 3, valueb);
        mutableMatrixA.sub(mutableMatrixB);

        /*
         * 预期结果
         * -6 -6 -6
         * -6 -6 -6
         */
        System.out.println("InmutableMatrix Sub Result:");
        inmutableMatrixRes.print();
        System.out.println("MutableMatrix Sub Result:");
        mutableMatrixA.print();

        //测试矩阵数乘
        inmutableMatrixA = new InmutableMatrix(2, 3, valuea);
        inmutableMatrixRes = inmutableMatrixA.scalarMultiply(2);

        mutableMatrixA = new MutableMatrix(2, 3, valuea);
        mutableMatrixA.scalarMultiply(2);

        /*
         * 预期结果
         * 2 4 6
         * 8 10 12
         */
        System.out.println("InmutableMatrix Scalar Multiply Result:");
        inmutableMatrixRes.print();
        System.out.println("MutableMatrix Scalar Multiply Result:");
        mutableMatrixA.print();

        //测试矩阵乘法
        inmutableMatrixA = new InmutableMatrix(2, 3, valuea);
        inmutableMatrixB = new InmutableMatrix(3, 2, valuec);
        inmutableMatrixRes = inmutableMatrixA.multiply(inmutableMatrixB);

        mutableMatrixA = new MutableMatrix(2, 3, valuea);
        mutableMatrixB = new MutableMatrix(3, 2, valuec);
        mutableMatrixA.multiply(mutableMatrixB);

        /*
         * 预期结果
         * 22 28
         * 49 64
         */
        System.out.println("InmutableMatrix Multiply Result:");
        inmutableMatrixRes.print();
        System.out.println("MutableMatrix Multiply Result:");
        mutableMatrixA.print();

        // 矩阵链式运算

        inmutableMatrixA = new InmutableMatrix(2, 3, valuea);
        inmutableMatrixB = new InmutableMatrix(3, 2, valuec);
        
        inmutableMatrixRes = inmutableMatrixA.add(inmutableMatrixA).sub(new InmutableMatrix(2, 3, valueb)).multiply(inmutableMatrixB);

        mutableMatrixA = new MutableMatrix(2, 3, valuea);
        mutableMatrixB = new MutableMatrix(3, 2, valuec);

        mutableMatrixA.add(mutableMatrixA).sub(new MutableMatrix(2, 3, valueb)).multiply(mutableMatrixB);

        /*
         * 预期结果
         * -32 -44
         * -5 -8
        */
        System.out.println("InmutableMatrix Chained Operations Result:");
        inmutableMatrixRes.print();
        System.out.println("MutableMatrix Chained Operations Result:");
        mutableMatrixA.print();

        // 不同大小的矩阵执行加法，应该抛出异常
        try {
            inmutableMatrixA = new InmutableMatrix(2, 3, valuea);
            inmutableMatrixB = new InmutableMatrix(3, 2, valuec);
            inmutableMatrixRes = inmutableMatrixA.add(inmutableMatrixB);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            mutableMatrixA = new MutableMatrix(2, 3, valuea);
            mutableMatrixB = new MutableMatrix(3, 2, valuec);
            mutableMatrixA.add(mutableMatrixB);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // 不同大小的矩阵执行减法，应该抛出异常
        try {
            inmutableMatrixA = new InmutableMatrix(2, 3, valuea);
            inmutableMatrixB = new InmutableMatrix(3, 2, valuec);
            inmutableMatrixRes = inmutableMatrixA.sub(inmutableMatrixB);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            mutableMatrixA = new MutableMatrix(2, 3, valuea);
            mutableMatrixB = new MutableMatrix(3, 2, valuec);
            mutableMatrixA.sub(mutableMatrixB);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // 不匹配的矩阵执行乘法，应该抛出异常
        try {
            inmutableMatrixA = new InmutableMatrix(2, 3, valuea);
            inmutableMatrixRes = inmutableMatrixA.multiply(inmutableMatrixA);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            mutableMatrixA = new MutableMatrix(2, 3, valuea);
            mutableMatrixA.multiply(mutableMatrixA);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 相互构造测试
        inmutableMatrixA = new InmutableMatrix(new MutableMatrix(2, 3, valuea));
        mutableMatrixA = new MutableMatrix(new InmutableMatrix(2, 3, valuea));

        /**
         * 预期结果
         * 1 2 3
         * 4 5 6
         */
        System.out.println("InmutableMatrix create Result:");
        inmutableMatrixA.print();
        System.out.println("MutableMatrix create Result:");
        mutableMatrixA.print();

        // 性能测试
        // 初始化随机矩阵
        int[][] values1 = new int[size][size];
        int[][] values2 = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                values1[i][j] = (int) (Math.random() * randomValue);
                values2[i][j] = (int) (Math.random() * randomValue);
            }
        }
        // 创建 MutableMatrix 对象
        MutableMatrix mMatrix1 = new MutableMatrix(size, size, values1);
        MutableMatrix mMatrix2 = new MutableMatrix(size, size, values2);

        // 创建 InmutableMatrix 对象
        InmutableMatrix imMatrix1 = new InmutableMatrix(size, size, values1);
        InmutableMatrix imMatrix2 = new InmutableMatrix(size, size, values2);

        

        // 执行操作并记录时间
        testOperations(imMatrix1, imMatrix2);
        testOperations(mMatrix1, mMatrix2);
    }
    
    private static void testOperations(InmutableMatrix matrix1, InmutableMatrix matrix2) throws Exception {
        long startTime = System.nanoTime();
        matrix1.add(matrix2);
        long addEndTime = System.nanoTime();
        System.out.println("InmutableMatrix 加法时间： " + (addEndTime - startTime) + " 纳秒");

        startTime = System.nanoTime();
        matrix1.sub(matrix2);
        long subEndTime = System.nanoTime();
        System.out.println("InmutableMatrix 减法时间： " + (subEndTime - startTime) + " 纳秒");

        startTime = System.nanoTime();
        matrix1.scalarMultiply(randomValue);
        long scalarMultiplicationEndTime = System.nanoTime();
        System.out.println("InmutableMatrix 标量乘法时间： " + (scalarMultiplicationEndTime - startTime) + " 纳秒");

        startTime = System.nanoTime();
        matrix1.multiply(matrix2);
        long multiplicationEndTime = System.nanoTime();
        System.out.println("InmutableMatrix 矩阵乘法时间： " + (multiplicationEndTime - startTime) + " 纳秒");
    }

    private static void testOperations(MutableMatrix matrix1, MutableMatrix matrix2) throws Exception {
        long startTime = System.nanoTime();
        matrix1.add(matrix2);
        long addEndTime = System.nanoTime();
        System.out.println("MutableMatrix 加法时间： " + (addEndTime - startTime) + " 纳秒");

        startTime = System.nanoTime();
        matrix1.sub(matrix2);
        long subEndTime = System.nanoTime();
        System.out.println("MutableMatrix 减法时间： " + (subEndTime - startTime) + " 纳秒");

        startTime = System.nanoTime();
        matrix1.scalarMultiply(randomValue);
        long scalarMultiplicationEndTime = System.nanoTime();
        System.out.println("MutableMatrix 标量乘法时间： " + (scalarMultiplicationEndTime - startTime) + " 纳秒");

        startTime = System.nanoTime();
        matrix1.multiply(matrix2);
        long multiplicationEndTime = System.nanoTime();
        System.out.println("MutableMatrix 矩阵乘法时间： " + (multiplicationEndTime - startTime) + " 纳秒");
    }
}

