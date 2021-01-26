package www.pactera.com.coverage.project.dao.objectDo;

public class PortRelevanceDO {
    /**
     * 唯一主键id
     */
    private Integer id;
    /**
     *  项目名称
     */
    private String projectName;
    /**
     *  端口号
     */
    private String port;
    /**
     *  是否删除
     */
    private String isDelete;

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public PortRelevanceDO(Integer id, String projectName, String port, String isDelete) {
        this.id = id;
        this.projectName = projectName;
        this.port = port;
        this.isDelete = isDelete;
    }

    public PortRelevanceDO() {

    }

    @Override
    public String toString() {
        return "PortRelevanceDO{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", port='" + port + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}
