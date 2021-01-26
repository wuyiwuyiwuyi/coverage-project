package www.pactera.com.coverage.project.common.Dto;


public class ProjectBaseInfo {
    /**
     * 服务名称
     */
    private String projectName;
    /**
     * 服务的版本号
     */
    private String versionNumber;
    /**
     *  服务状态
     */
    private String status;
    /**
     *  阶段
     */
    private String phase;

    /**
     * 操作的用户
     */
    private String operator;

    /**
     * 时间
     * @return
     */
    private String time;

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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ProjectBaseInfo(String projectName, String versionNumber, String status, String phase, String operator, String time) {
        this.projectName = projectName;
        this.versionNumber = versionNumber;
        this.status = status;
        this.phase = phase;
        this.operator = operator;
        this.time = time;
    }

    public ProjectBaseInfo() {

    }


    @Override
    public String toString() {
        return "ProjectBaseInfo{" +
                "projectName='" + projectName + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", status='" + status + '\'' +
                ", phase='" + phase + '\'' +
                ", operator='" + operator + '\'' +
                ", time='" + time + '\'' +
                '}';
    }


}
