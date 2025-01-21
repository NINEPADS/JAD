/*
 * @Author: NINEPADS 1578066861@qq.com
 * @Date: 2024-11-07 20:27:18
 * @LastEditors: NINEPADS 1578066861@qq.com
 * @LastEditTime: 2024-11-09 14:28:24
 * @FilePath: \homework3\src\searchEngine.java
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import java.util.Scanner;

/**
 * 主类，用于启动搜索引擎并处理用户输入的命令。
 */
public class SearchEngine {

    /**
     * 主方法，程序入口。
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        try {
            // 创建扫描器对象，用于读取用户输入
            Scanner scanner = new Scanner(System.in);
            // 创建搜索对象
            Search search = new Search();
            System.out.println("Please enter the command:");
            // 读取用户输入的命令
            String cmd = scanner.nextLine();
            // 当用户输入的命令不是退出命令时，循环处理命令
            while (!cmd.equals("\\q")) {
                switch (cmd) {
                    case "\\createIndex":
                        // 创建索引
                        Index.create();
                        // 重新初始化搜索对象
                        search = new Search();
                        break;
                    case "\\keyWordSearch":
                        System.out.println("Please enter the key word:");
                        // 读取关键词并进行预处理（转小写，去除非字母数字字符）
                        String keyWords = scanner.nextLine().toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
                        // 调用搜索对象的关键词搜索方法
                        search.searchKeyWord(keyWords);
                        break;
                    case "\\sentenceSearch":
                        System.out.println("Please enter the sentence:");
                        // 读取句子并进行预处理（转小写，去除非字母数字字符）
                        String sentence = scanner.nextLine().toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
                        // 调用搜索对象的句子搜索方法
                        search.searchSentence(sentence);
                        break;
                    case "\\keyWordFuzzilySearch":
                        System.out.println("Please enter the key word:");
                        // 读取关键词并进行预处理（转小写，去除非字母数字字符）
                        keyWords = scanner.nextLine().toLowerCase().replaceAll("[^a-zA-Z0-9]", " ");
                        // 调用搜索对象的模糊关键词搜索方法
                        search.searchKeyWordFuzzily(keyWords);
                        break;
                    default:
                        break;
                }
                System.out.println("...[Done]...");
                System.out.println("Please enter the command:");
                // 读取下一个命令
                cmd = scanner.nextLine();
            }
            // 关闭扫描器
            scanner.close();
            System.out.println("...[quit]...");
        } catch (Exception e) {
            // 捕获并打印异常信息
            e.printStackTrace();
        }
    }
}
