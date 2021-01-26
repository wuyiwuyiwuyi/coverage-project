package www.pactera.com.coverage.project.dao.objectDo;

import java.io.Serializable;
import java.util.Date;

public class SourceClassCoverageDO implements Serializable {

    private Integer id;

    private String projectName;

    private String versionNumber;

    private String operator;

    private Date operationTime;

    private String packageName;

    private String sourceName;

    private String coverLine;

    private String missLine;

    private String notLine;

    private String totalLineNumber;

    private String coverLineNumber;

    private String sourceCoverageRate;


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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getCoverLine() {
        return coverLine;
    }

    public void setCoverLine(String coverLine) {
        this.coverLine = coverLine;
    }

    public String getMissLine() {
        return missLine;
    }

    public void setMissLine(String missLine) {
        this.missLine = missLine;
    }

    public String getNotLine() {
        return notLine;
    }

    public void setNotLine(String notLine) {
        this.notLine = notLine;
    }

    public String getTotalLineNumber() {
        return totalLineNumber;
    }

    public void setTotalLineNumber(String totalLineNumber) {
        this.totalLineNumber = totalLineNumber;
    }

    public String getCoverLineNumber() {
        return coverLineNumber;
    }

    public void setCoverLineNumber(String coverLineNumber) {
        this.coverLineNumber = coverLineNumber;
    }

    public String getSourceCoverageRate() {
        return sourceCoverageRate;
    }

    public void setSourceCoverageRate(String sourceCoverageRate) {
        this.sourceCoverageRate = sourceCoverageRate;
    }

    public SourceClassCoverageDO(Integer id, String projectName, String versionNumber, String operator, Date operationTime, String packageName, String sourceName, String coverLine, String missLine, String notLine, String totalLineNumber, String coverLineNumber, String sourceCoverageRate) {
        this.id = id;
        this.projectName = projectName;
        this.versionNumber = versionNumber;
        this.operator = operator;
        this.operationTime = operationTime;
        this.packageName = packageName;
        this.sourceName = sourceName;
        this.coverLine = coverLine;
        this.missLine = missLine;
        this.notLine = notLine;
        this.totalLineNumber = totalLineNumber;
        this.coverLineNumber = coverLineNumber;
        this.sourceCoverageRate = sourceCoverageRate;
    }

    public SourceClassCoverageDO() {

    }

    @Override
    public String toString() {
        return "SourceClassCoverageDO{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", operator='" + operator + '\'' +
                ", operationTime='" + operationTime + '\'' +
                ", packageName='" + packageName + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", coverLine='" + coverLine + '\'' +
                ", missLine='" + missLine + '\'' +
                ", notLine='" + notLine + '\'' +
                ", totalLineNumber='" + totalLineNumber + '\'' +
                ", coverLineNumber='" + coverLineNumber + '\'' +
                ", sourceCoverageRate='" + sourceCoverageRate + '\'' +
                '}';
    }
}
