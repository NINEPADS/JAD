package entity;

import java.sql.Timestamp;

public class Record {
    // 私有整型变量id，用于存储记录的唯一标识符
    private int id;
    // 私有字符串变量whiteUsername，用于存储白方玩家的用户名
    private String whiteUsername;
    // 私有字符串变量blackUsername，用于存储黑方玩家的用户名
    private String blackUsername;
    // 私有Timestamp对象startTime，用于存储游戏开始的时间戳
    private Timestamp startTime;
    // 私有Timestamp对象endTime，用于存储游戏结束的时间戳
    private Timestamp endTime;

    public Record(int id, String whiteUsername, String blackUsername, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getWhiteUsername() {
        return whiteUsername;
    }
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
    public Timestamp getStartTime() {
        return startTime;
    }
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    public Timestamp getEndTime() {
        return endTime;
    }
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
