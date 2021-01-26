package www.pactera.com.coverage.project.service;


import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.*;

public interface ICoverageCollectService {

    ResponseData<CollectCoverageRespDTO> coverageCollect(CollectCoverageReqDTO reqDTO, String token);

    ResponseData<MergeCoverageRespDTO> coverageMerge(MergeCoverageReqDTO reqDTO);

    ResponseData<QueryCoverageRespDTO> coverageQuery(QueryCollectCoverageReqDTO reqDTO);

    ResponseData<ProjectCoverageRespDTO> queryProjectCoverage(ProjectCoverageReqDTO projectCoverageReqDTO);

    ResponseData<SourceCoverageRespDTO> querySourceCoverage(SourceCoverageReqDTO sourceCoverageReqDTO);

    ResponseData<SourcesGainDTO> gainSourceFile(SourceCoverageReqDTO reqDTO);

}
