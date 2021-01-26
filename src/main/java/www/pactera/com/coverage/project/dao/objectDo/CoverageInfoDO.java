package www.pactera.com.coverage.project.dao.objectDo;

import java.io.Serializable;
import java.util.Date;

public class CoverageInfoDO implements Serializable {

    private Integer id;

    private String projectName;

    private String versionNumber;

    private String phase;

    private String status;

    private String type;

    private String coverageLine;

    private String totalLine;

    private String coverageRate;

    private String operator;

    private Date operationTime;

    private String isDelete;

    private String isMerge;

    public String getIsMerge() {
        return isMerge;
    }

    public void setIsMerge(String isMerge) {
        this.isMerge = isMerge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoverageLine() {
        return coverageLine;
    }

    public void setCoverageLine(String coverageLine) {
        this.coverageLine = coverageLine;
    }

    public String getTotalLine() {
        return totalLine;
    }

    public void setTotalLine(String totalLine) {
        this.totalLine = totalLine;
    }

    public String getCoverageRate() {
        return coverageRate;
    }

    public void setCoverageRate(String coverageRate) {
        this.coverageRate = coverageRate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public CoverageInfoDO(Integer id, String projectName, String versionNumber, String phase, String status, String type, String coverageLine, String totalLine, String coverageRate, String operator, Date operationTime, String isDelete, String isMerge) {
        this.id = id;
        this.projectName = projectName;
        this.versionNumber = versionNumber;
        this.phase = phase;
        this.status = status;
        this.type = type;
        this.coverageLine = coverageLine;
        this.totalLine = totalLine;
        this.coverageRate = coverageRate;
        this.operator = operator;
        this.operationTime = operationTime;
        this.isDelete = isDelete;
        this.isMerge = isMerge;
    }

    public CoverageInfoDO() {

    }

    @Override
    public String toString() {
        return "CoverageInfoDO{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", phase='" + phase + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", coverageLine='" + coverageLine + '\'' +
                ", totalLine='" + totalLine + '\'' +
                ", coverageRate='" + coverageRate + '\'' +
                ", operator='" + operator + '\'' +
                ", operationTime=" + operationTime +
                ", isDelete='" + isDelete + '\'' +
                ", isMerge='" + isMerge + '\'' +
                '}';
    }
}
