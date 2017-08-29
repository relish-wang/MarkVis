package wang.relish.markvis;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author relish
 * @since 2017/08/29
 */
public class UploadDataCompat {

    /**
     * App启动次数
     */
    private SimpleLongProperty launchCount;
    /**
     * 此次统计开始时间
     */
    private SimpleStringProperty startTime;
    /**
     * 此次统计结束时间
     */
    private SimpleStringProperty endTime;
    /**
     * url过滤规则
     */
    private SimpleStringProperty filter;
    /**
     * 网络类型( 0 | 1 | 2)
     * 0: 总共
     * 1: Wi-Fi
     * 2: Cellular
     */
    private SimpleStringProperty networkType;
    /**
     * 流量大小（单位：KByte）
     */
    private SimpleLongProperty size;
    /**
     * (对应filter的)访问次数
     */
    private SimpleLongProperty reqCount;

    public UploadDataCompat(UploadData d) {
        this(d.getLaunchCount(), d.getStartTime(), d.getEndTime(), d.getFilter(), d.getNetworkType(), d.getSize(), d.getReqCount());
    }

    public UploadDataCompat(long launchCount, long startTime, long endTime, String filter, int networkType, long size, long reqCount) {
        this.launchCount = new SimpleLongProperty(launchCount);
        this.startTime = new SimpleStringProperty(TimeUtil.longToString(startTime));
        this.endTime = new SimpleStringProperty(TimeUtil.longToString(endTime));
        this.filter = new SimpleStringProperty(filter);
        this.networkType = new SimpleStringProperty(networkType(networkType));
        this.size = new SimpleLongProperty(size);
        this.reqCount = new SimpleLongProperty(reqCount);
    }

    public long getLaunchCount() {
        return launchCount.get();
    }

    public void setLaunchCount(long launchCount) {
        this.launchCount = new SimpleLongProperty(launchCount);
    }

    public String getStartTime() {
        return startTime.get();
    }

    public void setStartTime(String startTime) {
        this.startTime = new SimpleStringProperty(startTime);
    }

    public String getEndTime() {
        return endTime.get();
    }

    public void setEndTime(long endTime) {
        this.endTime = new SimpleStringProperty(TimeUtil.longToString(endTime));
    }

    public String getFilter() {
        return filter.get();
    }

    public void setFilter(String filter) {
        this.filter = new SimpleStringProperty(filter);
    }

    public String getNetworkType() {
        return networkType.get();
    }

    public void setNetworkType(int networkType) {
        this.networkType = new SimpleStringProperty(networkType(networkType));
    }

    public long getSize() {
        return size.get();
    }

    public void setSize(long size) {
        this.size = new SimpleLongProperty(size);
    }

    public long getReqCount() {
        return reqCount.get();
    }

    public void setReqCount(long reqCount) {
        this.reqCount = new SimpleLongProperty(reqCount);
    }


    private static String networkType(int type) {
        switch (type) {
            case 0:
                return "SUM";
            case 1:
                return "WIFI";
            case 2:
                return "Cellular";
            default:
                return type + "";
        }
    }
}
