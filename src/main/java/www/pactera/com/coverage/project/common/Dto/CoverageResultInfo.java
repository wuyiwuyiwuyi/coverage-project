package www.pactera.com.coverage.project.common.Dto;

import lombok.Builder;
import www.pactera.com.coverage.project.common.BaseEntity.BaseDTO;


@Builder
public class CoverageResultInfo extends BaseDTO {
    /**
     * 服务名称
     */
    private String projectName;
    /**
     * 版本号
     */
    private String versionNumber;
    /**
     * 状态
     */
    private String status;
    /**
     * 阶段
     */
    private String phase;
    /**
     * 操作时间
     */
    private String time;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 覆盖率
     */
    private String coverageRate;
    /**
     * 总行数
     */
    private String totalLineNumber;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCoverageRate() {
        return coverageRate;
    }

    public void setCoverageRate(String coverageRate) {
        this.coverageRate = coverageRate;
    }

    public String getTotalLineNumber() {
        return totalLineNumber;
    }

    public void setTotalLineNumber(String totalLineNumber) {
        this.totalLineNumber = totalLineNumber;
    }

    public CoverageResultInfo(String projectName, String versionNumber, String status, String phase, String time, String operator, String coverageRate, String totalLineNumber) {
        this.projectName = projectName;
        this.versionNumber = versionNumber;
        this.status = status;
        this.phase = phase;
        this.time = time;
        this.operator = operator;
        this.coverageRate = coverageRate;
        this.totalLineNumber = totalLineNumber;
    }

    public CoverageResultInfo() {

    }

    @Override
    public String toString() {
        return "CoverageResultInfo{" +
                "projectName='" + projectName + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", status='" + status + '\'' +
                ", phase='" + phase + '\'' +
                ", time='" + time + '\'' +
                ", operator='" + operator + '\'' +
                ", coverageRate='" + coverageRate + '\'' +
                ", totalLineNumber='" + totalLineNumber + '\'' +
                '}';
    }
}
