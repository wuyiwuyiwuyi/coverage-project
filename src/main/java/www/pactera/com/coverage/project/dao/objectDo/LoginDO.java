package www.pactera.com.coverage.project.dao.objectDo;

import java.io.Serializable;
import java.util.Date;

public class LoginDO implements Serializable {

    private Integer id;

    private String username;

    private String password;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public LoginDO(Integer id, String username, String password, Date createTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
    }

    public LoginDO() {

    }

    @Override
    public String toString() {
        return "LoginDO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
