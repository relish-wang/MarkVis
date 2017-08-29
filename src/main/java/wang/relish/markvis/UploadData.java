package wang.relish.markvis;


import java.io.Serializable;

/**
 * 统计结果
 * <p>
 * | launchCount |      startTime      |       endTime       |        filter        | networkType |  size(KB) |
 * | :---------: | :-----------------: | :-----------------: | :------------------: | :---------: | :-------: |
 * |     13      |    1501741909582    |     501742298025    |          \*          |      0      |     89    |
 * |     13      |    1501741909582    |     501742298025    |          \*          |      0      |     89    |
 * |     13      |    1501741909582    |     501742298025    |        \*.\*         |      1      |     88    |
 * |     13      |    1501741909582    |     501742298025    |        \*.\*         |      2      |      8    |
 * |     13      |    1501741909582    |     501742298025    |    \*.souche.com     |      1      |     51    |
 * |     13      |    1501741909582    |     501742298025    |    \*.souche.com     |      2      |      5    |
 * |     13      |    1501741909582    |     501742298025    | \*.update.souche.com |      1      |     25    |
 * |     13      |    1501741909582    |     501742298025    | \*.update.souche.com |      2      |      2    |
 *
 * @author Relish Wang
 * @since 2017/08/29
 */
final class UploadData implements Serializable {

    /**
     * App启动次数
     */
    private long launchCount;
    /**
     * 此次统计开始时间
     */
    private long startTime;
    /**
     * 此次统计结束时间
     */
    private long endTime;
    /**
     * url过滤规则
     */
    private String filter;
    /**
     * 网络类型( 0 | 1 | 2)
     * 0: 总共
     * 1: Wi-Fi
     * 2: Cellular
     */
    private int networkType;
    /**
     * 流量大小（单位：KByte）
     */
    private long size;
    /**
     * (对应filter的)访问次数
     */
    private long reqCount;

    public long getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(long launchCount) {
        this.launchCount = launchCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getReqCount() {
        return reqCount;
    }

    public void setReqCount(long reqCount) {
        this.reqCount = reqCount;
    }
}
