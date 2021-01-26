package www.pactera.com.coverage.project.dao.objectDao;

import org.apache.ibatis.annotations.Param;
import www.pactera.com.coverage.project.dao.objectDo.CoverageInfoDO;

import java.util.Date;
import java.util.List;

public interface CoverageInfoDoMapper {

    void insert(CoverageInfoDO coverageInfoDO);

    CoverageInfoDO selectProjectCoverage(@Param("projectName")String projectName,@Param("versionNumber")String versionNumber,@Param("operator")String operator);

    List<CoverageInfoDO> selectAll();

    List<CoverageInfoDO> selectCoverageInfoDOLists(@Param("projectName")String projectName,
                                                   @Param("versionNumber")String versionNumber,
                                                   @Param("operator")String operator,
                                                   @Param("startTime")Date startTime,
                                                   @Param("endTime")Date endTime,
                                                   @Param("status")String status);

    void update(@Param("coverageInfoDO") CoverageInfoDO coverageInfoDO);


    void updateStatus(@Param("projectName")String projectName,
                      @Param("versionNumber")String versionNumber,
                      @Param("operator")String operator,
                      @Param("status")String status);
}
