package www.pactera.com.coverage.project.common.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryUserAddRespDTO {
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

    private List<QueryUserAddDTO> queryUserAddDTOS;

}
