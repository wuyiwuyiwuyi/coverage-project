package www.pactera.com.coverage.project.dao.objectDao;

import org.apache.ibatis.annotations.Param;
import www.pactera.com.coverage.project.dao.objectDo.SourcePathInfoDO;

import java.util.List;

public interface SourcePathInfoDOMapper {

    void insert(@Param("sourcePathInfoDO") SourcePathInfoDO sourcePathInfoDO);

    String selectPackageName(@Param("projectName") String projectName,
                             @Param("versionNumber") String versionNumber,
                             @Param("operator") String operator,
                             @Param("sourceName") String sourceName);

    List<SourcePathInfoDO> selectProjectFile(@Param("projectName")String projectName, @Param("versionNumber")String versionNumber, @Param("operator")String operator);

}
