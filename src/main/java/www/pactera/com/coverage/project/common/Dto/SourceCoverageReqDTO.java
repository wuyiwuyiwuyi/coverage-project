package www.pactera.com.coverage.project.common.Dto;

import lombok.*;
import www.pactera.com.coverage.project.common.BaseEntity.BaseDTO;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SourceCoverageReqDTO extends BaseDTO {
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 版本号
     */
    private String versionNumber;
    /**
     *  操作人
     */
    private String operator;
    /**
     * java源文件名称
     */
    private String sourceName;

}
