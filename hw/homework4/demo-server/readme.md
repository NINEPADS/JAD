# 程序运行指南（服务端版）

* 使用的编程语言为 `Java (OpenJDK v18.0.2)`
* 使用的IDE为 `IntelliJ IDEA (v2024.2.4)`
* 使用的包管理工具为 `Maven (v4.0.0)`
* 使用的数据库是 `MySQL (v5.7.43)`

在运行程序前**一定要配置好`Maven`**，这关系到程序是否能使用需要的第三方库\
（尤其是JavaFx在java11之后已经被移出JDK了，需要通过Maven来自动下载）

此外，还需要配置好数据库，你需要创建一个数据库并通过 `src/main/resources/db/init.sql`
来建立数据表，然后你要通过更改`src/main/java/Controller/DatabaseManager.java`中的数据库配置，来使得你的项目能成功连上你的本地数据库
```java
// DatabaseManager.java
    // url
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fiveChess";
    // 你的数据库用户名
    private static final String DB_USER = "root";
    // 你的数据库密码
    private static final String DB_PASSWORD = "123456";
```
连接上数据库后，你可以运行`src/main/resources/db/data-init.sql`中的语句来增加一些账号，方便登录多个客户端来测试。

之后你应该就能运行服务端程序了：
通过运行com/example/demo/ServerDemo.java中的主函数来开启服务端，服务端监听端口为3495（注意避免冲突）
如果要更改监听端口，记得也要同步更改客户端的发送目的端口呀。

如果还有问题，联系我（虽然我未必知道如何解决）



