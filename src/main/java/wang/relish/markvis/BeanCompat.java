
package wang.relish.markvis;

import javafx.beans.property.SimpleStringProperty;

public class BeanCompat {
  private SimpleStringProperty launchCount;

  private SimpleStringProperty filter;

  private SimpleStringProperty reqCount;

  private SimpleStringProperty size;

  private SimpleStringProperty startTime;

  private SimpleStringProperty endTime;

  private SimpleStringProperty networkType;

  public BeanCompat(Bean bean) {
    this.launchCount = new SimpleStringProperty(bean.getLaunchCount() + "");
    this.filter = new SimpleStringProperty(bean.getFilter() + "");
    this.reqCount = new SimpleStringProperty(bean.getReqCount() + "");
    this.size = new SimpleStringProperty(bean.getSize() + "");
    this.startTime = new SimpleStringProperty(bean.getStartTime() + "");
    this.endTime = new SimpleStringProperty(bean.getEndTime() + "");
    this.networkType = new SimpleStringProperty(bean.getNetworkType() + "");
  }

  public String getLaunchCount() {
    return this.launchCount.get();
  }

  public void setLaunchCount(String launchCount) {
    this.launchCount = new SimpleStringProperty(launchCount);
  }

  public String getFilter() {
    return this.filter.get();
  }

  public void setFilter(String filter) {
    this.filter = new SimpleStringProperty(filter);
  }

  public String getReqCount() {
    return this.reqCount.get();
  }

  public void setReqCount(String reqCount) {
    this.reqCount = new SimpleStringProperty(reqCount);
  }

  public String getSize() {
    return this.size.get();
  }

  public void setSize(String size) {
    this.size = new SimpleStringProperty(size);
  }

  public String getStartTime() {
    return this.startTime.get();
  }

  public void setStartTime(String startTime) {
    this.startTime = new SimpleStringProperty(startTime);
  }

  public String getEndTime() {
    return this.endTime.get();
  }

  public void setEndTime(String endTime) {
    this.endTime = new SimpleStringProperty(endTime);
  }

  public String getNetworkType() {
    return this.networkType.get();
  }

  public void setNetworkType(String networkType) {
    this.networkType = new SimpleStringProperty(networkType);
  }
}