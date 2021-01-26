package www.pactera.com.coverage.project.dao.objectDao;

import org.apache.ibatis.annotations.Param;
import www.pactera.com.coverage.project.dao.objectDo.PortRelevanceDO;


public interface PortRelevanceDOMapper {

    PortRelevanceDO selectPort(@Param("projectName")String projectName);

}
