package www.pactera.com.coverage.project.dao.objectDo;

import java.io.Serializable;
import java.util.Date;

public class CoverageDO implements Serializable {
    private Integer id;

    private String serialId;

    private String serverName;

    private String versionNumber;

    private String status;

    private String phase;

    private String handleUser;

    private String businessDirection;

    private String coverageRate;

    private String totalLine;

    private String reportUrl;

    private Date generateTime;

    private String isDelete;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getHandleUser() {
        return handleUser;
    }

    public void setHandleUser(String handleUser) {
        this.handleUser = handleUser;
    }

    public String getBusinessDirection() {
        return businessDirection;
    }

    public void setBusinessDirection(String businessDirection) {
        this.businessDirection = businessDirection;
    }

    public String getCoverageRate() {
        return coverageRate;
    }

    public void setCoverageRate(String coverageRate) {
        this.coverageRate = coverageRate;
    }

    public String getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(String totalLine) {
        this.totalLine = totalLine;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public Date getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        this.generateTime = generateTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "CoverageDO{" +
                "id=" + id +
                ", serialId='" + serialId + '\'' +
                ", serverName='" + serverName + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", status='" + status + '\'' +
                ", phase='" + phase + '\'' +
                ", handleUser='" + handleUser + '\'' +
                ", businessDirection='" + businessDirection + '\'' +
                ", coverageRate='" + coverageRate + '\'' +
                ", totalLine='" + totalLine + '\'' +
                ", reportUrl='" + reportUrl + '\'' +
                ", generateTime=" + generateTime +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}