package www.pactera.com.coverage.project.component.report;

import www.pactera.com.coverage.project.common.Dto.CollectCoverageReqDTO;
import www.pactera.com.coverage.project.component.data.execution.ExecutionData;
import www.pactera.com.coverage.project.component.data.session.SessionInfo;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface IMergeVisitor extends IReportGroupVisitor{

    void visitInfo(List<SessionInfo> sessionInfos,
                   Collection<ExecutionData> executionData,String project,String versionNumber,String username) throws IOException;

    void visitEnd() throws IOException;

}
