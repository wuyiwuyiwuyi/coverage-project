package www.pactera.com.coverage.project.common.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryUserAddReqDTO {
    /**
     * 页大小
     */
    private int pageSize;
    /**
     * 当前页
     */
    private int currentPage;
}
