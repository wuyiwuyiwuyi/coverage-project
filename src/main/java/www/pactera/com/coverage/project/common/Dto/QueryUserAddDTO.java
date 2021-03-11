package www.pactera.com.coverage.project.common.Dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryUserAddDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 状态
     */
    private String status;
    /**
     * 授权时间
     */
    private String authorizationData;
    /**
     * 角色
     */
    private String role;

}
