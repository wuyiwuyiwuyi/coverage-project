package www.pactera.com.coverage.project.common.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import www.pactera.com.coverage.project.common.BaseEntity.BaseDTO;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryCollectCoverageReqDTO extends BaseDTO {
    /**
     * 服务名称
     */
    private String projectName;
    /**
     * 版本号
     */
    private String versionNumber;
    /**
     * 状态
     */
    private String status;
    /**
     * 时间范围
     */
    private Date startTime;
    /**
     * 截止时间
     */
    private Date endTime;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 页大小
     */
    private int pageSize;
    /**
     * 当前页
     */
    private int currentPage;
}
