package www.pactera.com.coverage.project.common.Dto;

import lombok.Data;

@Data
public class UserDTO {

    private int id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
    /**
     * 角色
     */
    private String role;

}
