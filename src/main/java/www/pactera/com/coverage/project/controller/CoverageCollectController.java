package www.pactera.com.coverage.project.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.*;
import www.pactera.com.coverage.project.service.ICoverageCollectService;
import www.pactera.com.coverage.project.share.ICoverageCollectShare;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
public class CoverageCollectController implements ICoverageCollectShare {

    @Autowired
    private ICoverageCollectService coverageCollectService;

    @Override
    public ResponseData<CollectCoverageRespDTO> CoverageCollect(@Valid CollectCoverageReqDTO reqDTO) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");

        //log.error("收集项目覆盖率开始：req={}",reqDTO);
        ResponseData<CollectCoverageRespDTO> resp = coverageCollectService.coverageCollect(reqDTO,token);
        //log.error("收集项目覆盖率结束：resp={}",resp);
        return resp;
    }

    @Override
    public ResponseData<MergeCoverageRespDTO> CoverageMerge(@Valid MergeCoverageReqDTO reqDTO) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        //log.error("合并项目覆盖率请求开始：req={}",reqDTO);
        ResponseData<MergeCoverageRespDTO> resp = coverageCollectService.coverageMerge(reqDTO,token);
        //log.error("合并项目覆盖率结果返回：resp={}",resp);
        return resp;
    }

    @Override
    public ResponseData<QueryCoverageRespDTO> queryCoverage(@Valid QueryCollectCoverageReqDTO reqDTO) {
        //log.error("按条件查询整体项目覆盖率开始：req={}",reqDTO);
        ResponseData<QueryCoverageRespDTO> resp =  coverageCollectService.coverageQuery(reqDTO);
        //log.error("按条件查询整体项目覆盖率结束：resp={}",resp);
        return resp;
    }


    @Override
    public ResponseData<ProjectCoverageRespDTO> queryProjectCoverage(@Valid ProjectCoverageReqDTO projectCoverageReqDTO) {
        //log.error("查询项目覆盖率开始：req={}",projectCoverageReqDTO);
        ResponseData<ProjectCoverageRespDTO> resp = coverageCollectService.queryProjectCoverage(projectCoverageReqDTO);
        //log.error("查询项目覆盖率结束：resp={}",resp);
        return resp;
    }

    @Override
    public ResponseData<SourceCoverageRespDTO> querySourceCoverage(@Valid SourceCoverageReqDTO sourceCoverageReqDTO) {
        //log.debug("查询具体的文件覆盖详情请求开始：req={}",sourceCoverageReqDTO);
        ResponseData<SourceCoverageRespDTO> resp = coverageCollectService.querySourceCoverage(sourceCoverageReqDTO);
        //log.debug("查询具体的文件覆盖详情请求结束：resp={}",resp);
        return resp;
    }

    @Override
    public ResponseData<SourcesGainDTO> gainSourceFile(@Valid SourceCoverageReqDTO reqDTO) {

        ResponseData<SourcesGainDTO> resp = coverageCollectService.gainSourceFile(reqDTO);

        return resp;
    }


}
