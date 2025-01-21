package Controller;

import entity.ClientInfo;
import entity.FiveChess;
import entity.Game;
import entity.Record;
import enums.Side;
import enums.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.util.Set;

public class ClientHandler implements Runnable {
    private final ClientInfo clientInfo;// 当前客户端的信息
    private final Map<String, ClientInfo> clients;// 所有已连接客户端的信息
    private final Map<Integer, Game> waitingGames;// 等待中的游戏列表
    private final Map<Integer, Game> runningGames;// 进行中的游戏列表
    private final Set<String> loggedUsers;// 已登录的用户集合

    // 构造函数，初始化客户端处理器
    public ClientHandler(Socket socket, Map<String, ClientInfo> clients, Map<Integer, Game> waitingGames, Map<Integer, Game> runningGames, Set<String> loggedUsers) {
        this.clientInfo = new ClientInfo(); // 创建新的客户端信息对象
        clientInfo.setSocket(socket); // 设置客户端的套接字
        clientInfo.setState(State.RELAX); // 设置客户端的初始状态为放松状态
        this.clients = clients; // 初始化客户端信息映射
        this.waitingGames = waitingGames; // 初始化等待中游戏映射
        this.runningGames = runningGames; // 初始化进行中游戏映射
        this.loggedUsers = loggedUsers; // 初始化已登录用户集合
    }

    // 发送客户端列表给请求者
    public void sendClientsList(PrintWriter out) {
        if (!clientInfo.isLogged()) { // 如果客户端未登录，直接返回
            return;
        }
        synchronized (clients) { // 同步访问客户端信息映射
            StringBuilder response = new StringBuilder(); // 构建响应字符串
            response.append("clientList "); // 添加客户端列表标识
            for (Map.Entry<String, ClientInfo> entry : clients.entrySet()) { // 遍历所有客户端信息
                response.append(entry.getKey()); // 添加用户名
                response.append(",");
                response.append(entry.getValue().getSocket().getPort()); // 添加端口号
                response.append(",");
                response.append(entry.getValue().getState().getDescription()); // 添加状态描述
                response.append(" ");
            }
            out.println(response); // 输出响应字符串
        }
    }

    // 查询并发送所有历史记录给请求者
    public void queryAllHistory(PrintWriter out) {
        StringBuilder response = new StringBuilder(); // 构建响应字符串
        List<Record> records = DatabaseManager.queryAllRecords(clientInfo.getUsername()); // 查询所有历史记录
        if (records.isEmpty()) { // 如果没有历史记录
            out.println("noRecords"); // 输出无记录标识
        } else {
            response.append("records "); // 添加历史记录标识
            for (Record record : records) { // 遍历所有历史记录
                int id = record.getId(); // 获取记录ID
                String blackUsername = record.getBlackUsername().isEmpty() ? "无" : record.getBlackUsername(); // 获取黑方用户名
                String whiteUsername = record.getWhiteUsername().isEmpty() ? "无" : record.getWhiteUsername(); // 获取白方用户名
                Timestamp startTime = record.getStartTime(); // 获取开始时间
                Timestamp endTime = record.getEndTime(); // 获取结束时间
                response.append(id); // 添加记录ID
                response.append(",");
                response.append(blackUsername); // 添加黑方用户名
                response.append(",");
                response.append(whiteUsername); // 添加白方用户名
                response.append(",");
                response.append(startTime.toString().replace(" ", ".")); // 添加开始时间
                response.append(",");
                response.append(endTime.toString().replace(" ", ".")); // 添加结束时间
                response.append(" ");
            }
            out.println(response); // 输出响应字符串
        }
    }

    // 创建新游戏并通知请求者
    public void createGame(PrintWriter out) {
        if (!clientInfo.isLogged()) { // 如果客户端未登录，直接返回
            return;
        }
        System.out.println("Call createGame"); // 打印调用创建游戏日志
        Game game = new Game(); // 创建新游戏对象
        game.setFiveChess(new FiveChess(20, 20, 28)); // 设置五子棋棋盘大小和规则
        game.setClientWhite(clientInfo.getSocket()); // 设置白方客户端套接字
        game.setCurrentSide(Side.WHITE); // 设置当前轮到白方
        game.setCurrentState(State.WAIT); // 设置当前状态为等待中
        game.setUserIdWhite(clientInfo.getUserId()); // 设置白方用户ID
        game.SetUsernameWhite(clientInfo.getUsername()); // 设置白方用户名
        game.addContent(clientInfo.getUsername() + ":" + Side.WHITE.getDesc() + " "); // 添加游戏内容记录
        game.setStartTime(new Timestamp(System.currentTimeMillis())); // 设置游戏开始时间
        clientInfo.setSide(Side.WHITE); // 设置客户端当前角色为白方
        synchronized (waitingGames) { // 同步访问等待中游戏映射
            waitingGames.put(clientInfo.getSocket().getPort(), game); // 将游戏添加到等待中游戏映射中
        }
        clientInfo.setGame(game); // 设置客户端当前游戏对象
        System.out.println(game.hashCode()); // 打印游戏哈希码
        out.println("createGame " + game.hashCode()); // 输出创建游戏响应
    }

    // 加入已有游戏并通知请求者
    public void joinGame(PrintWriter out) {
        if (!clientInfo.isLogged()) { // 如果客户端未登录，直接返回
            return;
        }
        synchronized (waitingGames) { // 同步访问等待中游戏映射
            if (!waitingGames.isEmpty()) { // 如果等待中游戏不为空
                int key = waitingGames.keySet().iterator().next(); // 获取第一个等待中游戏的键值
                Game game = waitingGames.get(key); // 获取对应的游戏对象
                game.setClientBlack(clientInfo.getSocket()); // 设置黑方客户端套接字
                game.setCurrentState(State.RUN); // 设置当前状态为进行中
                game.setUserIdBlack(clientInfo.getUserId()); // 设置黑方用户ID
                game.SetUsernameBlack(clientInfo.getUsername()); // 设置黑方用户名
                game.addContent(clientInfo.getUsername() + ":" + Side.BLACK.getDesc() + " "); // 添加游戏内容记录
                clientInfo.setSide(Side.BLACK); // 设置客户端当前角色为黑方
                synchronized (runningGames) { // 同步访问进行中游戏映射
                    runningGames.put(clientInfo.getSocket().getPort(), game); // 将游戏添加到进行中游戏映射中
                }
                clientInfo.setGame(game); // 设置客户端当前游戏对象
                waitingGames.remove(key); // 从等待中游戏映射中移除该游戏
                out.println("joinGame " + clientInfo.getGame().hashCode()); // 输出加入游戏响应
                Socket otherSocket = game.getClientWhite(); // 获取白方客户端套接字
                try {
                    PrintWriter printWriter = new PrintWriter(otherSocket.getOutputStream(), true); // 创建输出流写入器
                    printWriter.println("startGame white"); // 向白方客户端发送开始游戏消息
                    out.println("startGame black"); // 向黑方客户端发送开始游戏消息
                } catch (IOException e) { // 捕获IO异常
                    System.err.println("启动失败" + e.getMessage()); // 打印错误信息
                }
            } else {
                out.println("noGame"); // 如果没有等待中游戏，输出无游戏标识
            }
        }
    }
    public void placeChess(PrintWriter out, String message) {
        // 检查用户是否已登录，如果未登录则直接返回
        if (!clientInfo.isLogged()) {
            return;
        }
        // 解析消息，获取棋子的坐标
        String[] splits = message.split(" ");
        int i = Integer.parseInt(splits[1]);
        int j = Integer.parseInt(splits[2]);
        // 构建响应消息
        String response = "placeChess " + i + " " + j;
        // 记录当前玩家放置棋子的位置
        clientInfo.getGame().addContent(clientInfo.getSide().getDesc() + ":放置棋子位置(" + i + "," + j + ") ");
        // 在五子棋游戏逻辑中放置棋子
        clientInfo.getGame().getFiveChess().play(i, j);

        // 向客户端发送响应消息
        out.println(response);
        Socket otherSocket;
        // 根据当前玩家的颜色选择对方的socket
        if (clientInfo.getSide() == Side.BLACK) {
            otherSocket = clientInfo.getGame().getClientWhite();
        } else {
            otherSocket = clientInfo.getGame().getClientBlack();
        }
        try {
            // 向对方玩家发送放置棋子的消息
            PrintWriter printWriter = new PrintWriter(otherSocket.getOutputStream(), true);
            printWriter.println("placeChess " + i + " " + j);
            // 判断游戏是否结束，如果结束则处理游戏结束逻辑
            if (!clientInfo.getGame().getFiveChess().judgeGame(i, j, clientInfo.getGame().getFiveChess().getCurrentSide() == Side.BLACK ? Side.WHITE : clientInfo.getSide())) {
                Side winner = clientInfo.getGame().getFiveChess().getCurrentSide() == Side.BLACK ? Side.WHITE : clientInfo.getSide();
                clientInfo.getGame().addContent("赢家:" + winner.getDesc() + " ");
                overGame();
            }
        } catch (IOException e) {
            // 捕获并打印IO异常信息
            System.err.println("无法放置" + e.getMessage());
        }
    }

    public void quitGame(PrintWriter out) {
        // 检查用户是否已登录，如果未登录则直接返回
        if (!clientInfo.isLogged()) {
            return;
        }
        // 如果当前有进行中的游戏
        if (clientInfo.getGame() != null) {
            Socket ClientA = clientInfo.getGame().getClientBlack();
            Socket ClientB = clientInfo.getGame().getClientWhite();
            // 记录对局终止的信息
            clientInfo.getGame().addContent("对局终止 ");
            try {
                // 通知黑方玩家退出游戏
                if (ClientA != null) {
                    PrintWriter printWriter = new PrintWriter(ClientA.getOutputStream(), true);
                    synchronized (waitingGames) {
                        waitingGames.remove(ClientA.getPort());
                    }
                    synchronized (runningGames) {
                        runningGames.remove(ClientA.getPort());
                    }
                    printWriter.println("quitGame");
                }
                // 通知白方玩家退出游戏
                if (ClientB != null) {
                    PrintWriter printWriter = new PrintWriter(ClientB.getOutputStream(), true);
                    synchronized (waitingGames) {
                        waitingGames.remove(ClientB.getPort());
                    }
                    synchronized (runningGames) {
                        runningGames.remove(ClientB.getPort());
                    }
                    printWriter.println("quitGame");
                }
            } catch (IOException e) {
                // 捕获并打印IO异常信息
                System.err.println("无法退出对局: " + e.getMessage());
            }
            // 记录游戏结果到数据库
            DatabaseManager.gameRecord(clientInfo.getGame().getUsernameBlack(), clientInfo.getGame().getUsernameWhite(), clientInfo.getGame().getContent(), clientInfo.getGame().getStartTime(), new Timestamp(System.currentTimeMillis()));
        }
        // 清空当前用户的对局信息
        clientInfo.setGame(null);
    }

    public void overGame() {
        // 检查用户是否已登录，如果未登录则直接返回
        if (!clientInfo.isLogged()) {
            return;
        }
        // 如果当前有进行中的游戏
        if (clientInfo.getGame() != null) {
            Socket ClientA = clientInfo.getGame().getClientBlack();
            Socket ClientB = clientInfo.getGame().getClientWhite();
            // 记录对局结束的信息
            clientInfo.getGame().addContent("对局结束 ");
            try {
                // 通知黑方玩家游戏结束
                if (ClientA != null) {
                    PrintWriter printWriter = new PrintWriter(ClientA.getOutputStream(), true);
                    synchronized (waitingGames) {
                        waitingGames.remove(ClientA.getPort());
                    }
                    synchronized (runningGames) {
                        runningGames.remove(ClientA.getPort());
                    }
                    printWriter.println("overGame");
                }
                // 通知白方玩家游戏结束
                if (ClientB != null) {
                    PrintWriter printWriter = new PrintWriter(ClientB.getOutputStream(), true);
                    synchronized (waitingGames) {
                        waitingGames.remove(ClientB.getPort());
                    }
                    synchronized (runningGames) {
                        runningGames.remove(ClientB.getPort());
                    }
                    printWriter.println("overGame");
                }
            } catch (IOException e) {
                // 捕获并打印IO异常信息
                System.err.println("无法退出对局: " + e.getMessage());
            }
            // 记录游戏结果到数据库
            DatabaseManager.gameRecord(clientInfo.getGame().getUsernameBlack(), clientInfo.getGame().getUsernameWhite(), clientInfo.getGame().getContent(), clientInfo.getGame().getStartTime(), new Timestamp(System.currentTimeMillis()));
        }
        // 清空当前用户的对局信息
        clientInfo.setGame(null);
    }

    public void login(PrintWriter out, String inputLine) {
        // 检查用户是否已登录，如果已登录则返回登录失败信息
        if (clientInfo.isLogged()) {
            out.println("loginFailed logged");
            return;
        }
        // 解析输入行，获取用户名和密码
        String[] splits = inputLine.split(" ");
        if (splits.length == 3) {
            // 检查用户名是否已被使用，如果被使用则返回登录失败信息
            if (loggedUsers.contains(splits[1])) {
                out.println("loginFailed logged");
                return;
            }
            // 尝试登录，获取用户ID
            clientInfo.setUserId(DatabaseManager.login(splits[1], splits[2]));
            // 如果用户ID有效，设置用户为已登录状态
            if (clientInfo.getUserId() != -1) {
                clientInfo.setLogged(true);
            }
            // 如果登录成功，设置用户名并更新相关数据结构
            if (clientInfo.isLogged()) {
                clientInfo.setUsername(splits[1]);
                synchronized (loggedUsers) {
                    loggedUsers.add(clientInfo.getUsername());
                }
                synchronized (clients) {
                    clients.put(clientInfo.getUsername(), clientInfo);
                }
                // 输出登录成功的信息
                System.out.println("login now");
                out.println("login");
                out.println("login");
            } else {
                // 如果登录失败，返回错误信息
                out.println("loginFailed wrong");
            }
        }
    }

    public void register(PrintWriter out, String inputLine) {
        // 将输入的字符串按空格分割成数组
        String[] splits = inputLine.split(" ");

        // 检查分割后的数组长度是否为3，确保输入格式正确
        if (splits.length == 3) {
            // 调用DatabaseManager的register方法进行注册操作，传入用户名和密码
            boolean res = DatabaseManager.register(splits[1], splits[2]);

            // 根据注册结果返回不同的响应信息
            if (res) {
                // 如果注册成功，输出"register"
                out.println("register");
            } else {
                // 如果注册失败（可能因为用户已存在），输出"registered"
                out.println("registered");
            }
        }

        // 如果输入格式不正确，输出"registerFailed"
        out.println("registerFailed");
    }


    public void queryRecord(PrintWriter out, String inputLine) {
        // 打印调试信息，表示进入queryRecord方法
        System.out.println("queryRecord");

        // 将输入的字符串按空格分割成数组
        String[] splits = inputLine.split(" ");

        // 检查分割后的数组长度是否为2，确保输入格式正确
        if (splits.length == 2) {
            // 将第二个元素转换为整数ID
            int id = Integer.parseInt(splits[1]);

            // 调用DatabaseManager的queryRecord方法查询记录内容
            String content = DatabaseManager.queryRecord(id);

            // 如果查询结果不为空，则输出查询到的内容
            if (!content.isEmpty()) {
                out.println("queryRecord " + content);
            } else {
                // 如果查询结果为空，则输出recordNotFound
                out.println("recordNotFound");
            }
        }
    }

    @Override
    public void run() {
        // 使用try-with-resources语句自动关闭资源
        try (PrintWriter out = new PrintWriter(clientInfo.getSocket().getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientInfo.getSocket().getInputStream()))) {

            // 打印客户端连接信息
            System.out.println("客户端已连接: " + clientInfo.getSocket().getInetAddress());
            String inputLine;

            // 循环读取客户端发送的消息
            while ((inputLine = in.readLine()) != null) {
                // 打印收到的客户端消息
                System.out.println("收到客户端消息: " + inputLine);

                // 根据消息内容执行相应的操作
                switch (inputLine.split(" ")[0]) {
                    case "clientList" -> sendClientsList(out); // 发送客户端列表
                    case "createGame" -> createGame(out); // 创建游戏
                    case "joinGame" -> joinGame(out); // 加入游戏
                    case "placeChess" -> placeChess(out, inputLine); // 放置棋子
                    case "quitGame" -> quitGame(out); // 退出游戏
                    case "login" -> login(out, inputLine); // 登录
                    case "register" -> register(out, inputLine); // 注册
                    case "queryAllHistory" -> queryAllHistory(out); // 查询所有历史记录
                    case "record" -> queryRecord(out, inputLine); // 查询记录
                    default -> out.println("Hello from server!"); // 默认回复
                }
            }
        } catch (IOException e) {
            // 捕获IO异常并打印错误信息
            System.out.println("客户端已经断开连接: " + e.getMessage());
        } finally {
            // 确保在finally块中清理资源
            try {
                synchronized (clients) {
                    // 从客户端集合中移除当前客户端信息
                    clients.remove(clientInfo.getUsername());
                }
                synchronized (waitingGames) {
                    // 从等待游戏集合中移除当前客户端端口对应的游戏
                    waitingGames.remove(clientInfo.getSocket().getPort());
                }
                synchronized (runningGames) {
                    // 从运行游戏集合中移除当前客户端端口对应的游戏
                    runningGames.remove(clientInfo.getSocket().getPort());
                }
                synchronized (loggedUsers) {
                    // 从已登录用户集合中移除当前用户名
                    loggedUsers.remove(clientInfo.getUsername());
                }
                // 关闭客户端套接字
                clientInfo.getSocket().close();
            } catch (IOException e) {
                // 捕获IO异常并打印错误信息
                System.err.println("无法关闭客户端连接: " + e.getMessage());
            }
        }
    }

}
