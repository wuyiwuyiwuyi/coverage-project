package www.pactera.com.coverage.project.common.Dto;


import lombok.*;
import www.pactera.com.coverage.project.common.BaseEntity.BaseDTO;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectCoverageReqDTO extends BaseDTO {
    /**
     * 服务名称
     */
    @NonNull
    private String projectName;
    /**
     * 服务ip
     */
    @NonNull
    private String serverIp;
    /**
     * 服务版本号
     */
    @NonNull
    private String versionNumber;


}
