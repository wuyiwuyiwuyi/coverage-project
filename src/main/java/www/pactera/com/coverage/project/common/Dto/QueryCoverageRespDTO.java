package www.pactera.com.coverage.project.common.Dto;

import lombok.*;
import www.pactera.com.coverage.project.common.BaseEntity.BaseDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryCoverageRespDTO extends BaseDTO {

    /**
     * 总条数
     */
    private String totalCount;
    /**
     * 页大小
     */
    private String pageSize;
    /**
     * 当前页
     */
    private String currentPage;
    /**
     * 总页数
     */
    private String totalPage;
    /**
     * 是否有下一页
     */
    private boolean hasNextPage;

    private List<CoverageResultInfo> coverageResultInfos;



}
