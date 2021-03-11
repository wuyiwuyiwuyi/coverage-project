package www.pactera.com.coverage.project.dao.objectDao;

import org.apache.ibatis.annotations.Param;
import www.pactera.com.coverage.project.dao.objectDo.UserWhiteListDO;

import java.util.List;

public interface UserWhiteListDOMapper {

    void insert(@Param("userWhiteListDO") UserWhiteListDO userWhiteListDO);

    UserWhiteListDO selectOne(String employeeId);

    List<UserWhiteListDO> selectAll();

    void updateStatus(@Param("username")String username,
                      @Param("role")String role,
                      @Param("status")String status);

    List<UserWhiteListDO> selectApprovalTask();


    void deleteUser(@Param("employeeId")String employeeId,@Param("password") String password,@Param("role") String role);
}
