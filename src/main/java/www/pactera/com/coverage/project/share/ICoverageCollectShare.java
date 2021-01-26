package www.pactera.com.coverage.project.share;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.*;


import javax.validation.Valid;

@RequestMapping("/pactera")
public interface ICoverageCollectShare {

    /**
     * 收集项目的覆盖率
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/coverage/euq6yu/collect")
    @ResponseBody
    ResponseData<CollectCoverageRespDTO> CoverageCollect(@RequestBody @Valid CollectCoverageReqDTO reqDTO);
    /**
     * 合并子版本的覆盖率
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/coverage/56ghi6/merge")
    @ResponseBody
    ResponseData<MergeCoverageRespDTO> CoverageMerge(@RequestBody @Valid MergeCoverageReqDTO reqDTO);

    /**
     * 查询整体项目的覆盖率
     * @param reqDTO
     * @return
     */
    @RequestMapping("/coverage/5hkdf3/queryAll")
    @ResponseBody
    ResponseData<QueryCoverageRespDTO> queryCoverage(@RequestBody @Valid QueryCollectCoverageReqDTO reqDTO);

    /**
     * 收集指定项目的覆盖率
     * @param projectCoverageReqDTO
     * @return
     */
    @RequestMapping("/coverage/56ghi8/query")
    @ResponseBody
    ResponseData<ProjectCoverageRespDTO> queryProjectCoverage(@RequestBody @Valid ProjectCoverageReqDTO projectCoverageReqDTO);


    /**
     * 具体的Java文件的覆盖率数据
     * @param sourceCoverageReqDTO
     * @return
     */
    @RequestMapping("/coverage/3yin6w/querySource")
    @ResponseBody
    ResponseData<SourceCoverageRespDTO> querySourceCoverage(@RequestBody @Valid SourceCoverageReqDTO sourceCoverageReqDTO);

    /**
     *
     * @param
     * @param reqDTO
     */
    @RequestMapping("/coverage/4yic6w/ainSource")
    @ResponseBody
    ResponseData<SourcesGainDTO> gainSourceFile(@RequestBody @Valid SourceCoverageReqDTO reqDTO);
}
