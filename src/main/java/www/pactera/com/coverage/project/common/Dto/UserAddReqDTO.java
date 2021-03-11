package www.pactera.com.coverage.project.common.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddReqDTO {

    /**
     * 用户名
     */
    @NonNull
    private String username;
    /**
     * 密码
     */
    @NonNull
    private String password;
    /**
     * 角色
     */
    @NonNull
    private String role;

}
