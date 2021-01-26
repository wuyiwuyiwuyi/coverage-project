package www.pactera.com.coverage.project.dao.objectDao;

import org.apache.ibatis.annotations.Param;
import www.pactera.com.coverage.project.dao.objectDo.CoverageDO;

import java.util.List;

public interface CoverageDOMapper {

    List<CoverageDO> selectByCondition(@Param("coverageDO") CoverageDO record);

    int deleteByPrimaryKey(Integer id);

    int insert(CoverageDO record);

    List<CoverageDO> selectAll();

    void updateByCondition(@Param("coverageDO2") CoverageDO record);


}