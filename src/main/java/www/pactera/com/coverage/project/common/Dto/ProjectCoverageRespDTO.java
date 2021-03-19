package www.pactera.com.coverage.project.common.Dto;

import lombok.*;
import www.pactera.com.coverage.project.common.BaseEntity.BaseDTO;
import www.pactera.com.coverage.project.common.BaseEntity.Data;

import java.util.List;
import java.util.Map;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCoverageRespDTO extends BaseDTO {

    private String projectName;

    private String versionNumber;

    private String phase;

    private String status;

    private String type;

    private String coverageLine;

    private String totalLine;

    private String coverageRate;

    private String operator;

    private String operationTime;

    private Map<String, Data> coverageMap;

    private List<Map<String,Object>> data;





}
