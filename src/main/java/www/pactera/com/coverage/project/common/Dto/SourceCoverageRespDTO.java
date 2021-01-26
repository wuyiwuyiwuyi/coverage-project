package www.pactera.com.coverage.project.common.Dto;

import lombok.*;
import www.pactera.com.coverage.project.common.BaseEntity.BaseDTO;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SourceCoverageRespDTO extends BaseDTO {

    private String projectName;

    private String versionNumber;

    private String operator;

    private String operationTime;

    private String packageName;

    private String sourceName;

    private String coverLine;

    private String missLine;

    private String notLine;

    private String totalLineNumber;

    private String coverLineNumber;

    private String sourceCoverageRate;

}
