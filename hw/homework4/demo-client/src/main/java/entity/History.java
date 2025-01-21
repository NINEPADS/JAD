package entity;

import javafx.beans.property.SimpleStringProperty;

public class History {
    // 定义一个不可变的SimpleStringProperty对象，用于存储记录ID
    public final SimpleStringProperty recordId;
    // 定义一个不可变的SimpleStringProperty对象，用于存储白方用户名
    public final SimpleStringProperty whiteUserName;
    // 定义一个不可变的SimpleStringProperty对象，用于存储黑方用户名
    public final SimpleStringProperty blackUserName;
    // 定义一个不可变的SimpleStringProperty对象，用于存储开始时间
    public final SimpleStringProperty startTime;
    // 定义一个不可变的SimpleStringProperty对象，用于存储结束时间
    public final SimpleStringProperty endTime;
    public History(String recordId, String whiteUserName, String blackUserName, String startTime, String endTime) {
        this.recordId = new SimpleStringProperty(recordId);
        this.whiteUserName = new SimpleStringProperty(whiteUserName);
        this.blackUserName = new SimpleStringProperty(blackUserName);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
    }
    public String getRecordId() {
        return recordId.get();
    }
    public String getWhiteUserName() {
        return whiteUserName.get();
    }
    public String getBlackUserName() {
        return blackUserName.get();
    }
    public String getStartTime() {
        return startTime.get();
    }
    public String getEndTime() {
        return endTime.get();
    }

    public SimpleStringProperty blackUserNameProperty() {
        return blackUserName;
    }
    public SimpleStringProperty whiteUserNameProperty() {
        return whiteUserName;
    }
    public SimpleStringProperty startTimeProperty() {
        return startTime;
    }
    public SimpleStringProperty endTimeProperty() {
        return endTime;
    }
    public SimpleStringProperty recordIdProperty() {
        return recordId;
    }
    public String toString() {
        return "[username: " + whiteUserName.toString() + ", recordId: " + recordId.toString() + ", startTime: " + startTime + ", endTime: " + endTime + "]";
    }
}
