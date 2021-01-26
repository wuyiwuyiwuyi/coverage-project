package www.pactera.com.coverage.project.common.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import www.pactera.com.coverage.project.common.BaseEntity.BaseDTO;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCoverageReqDTO extends BaseDTO {
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目版本
     */
    private String versionNumber;
    /**
     * 操作人
     */
    private String operator;


}
