package www.pactera.com.coverage.project.dao.objectDo;

import java.util.Date;

public class UserWhiteListDO {

    private int id;

    private String employeeId;

    private String password;

    private String isAuthorization;

    private Date authorizationData;

    private String role;

    private String isDelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsAuthorization() {
        return isAuthorization;
    }

    public void setIsAuthorization(String isAuthorization) {
        this.isAuthorization = isAuthorization;
    }

    public Date getAuthorizationData() {
        return authorizationData;
    }

    public void setAuthorizationData(Date authorizationData) {
        this.authorizationData = authorizationData;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public UserWhiteListDO(int id, String employeeId, String password, String isAuthorization, Date authorizationData, String role, String isDelete) {
        this.id = id;
        this.employeeId = employeeId;
        this.password = password;
        this.isAuthorization = isAuthorization;
        this.authorizationData = authorizationData;
        this.role = role;
        this.isDelete = isDelete;
    }

    public UserWhiteListDO() {

    }
}
