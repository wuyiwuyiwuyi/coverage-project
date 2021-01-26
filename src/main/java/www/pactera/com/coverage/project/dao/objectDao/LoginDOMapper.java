package www.pactera.com.coverage.project.dao.objectDao;

import org.apache.ibatis.annotations.Param;
import www.pactera.com.coverage.project.dao.objectDo.LoginDO;


public interface LoginDOMapper {

    int insert(@Param("LoginDO") LoginDO loginDO);

    LoginDO selectByUsername(@Param("username")String um);

}
