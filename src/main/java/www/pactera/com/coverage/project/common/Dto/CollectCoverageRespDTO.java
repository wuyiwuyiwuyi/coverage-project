package www.pactera.com.coverage.project.common.Dto;

public class CollectCoverageRespDTO {

    /**
     * 覆盖率收集结果
     */
    private boolean result;

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public CollectCoverageRespDTO(boolean result) {
        this.result = result;
    }

    public CollectCoverageRespDTO() {

    }
}
