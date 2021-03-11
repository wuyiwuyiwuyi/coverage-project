package www.pactera.com.coverage.project.component.report;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import www.pactera.com.coverage.project.common.Dto.CollectCoverageReqDTO;
import www.pactera.com.coverage.project.commonTools.StringUtils;
import www.pactera.com.coverage.project.component.core.analysis.ICounter;
import www.pactera.com.coverage.project.component.core.analysis.ILine;
import www.pactera.com.coverage.project.component.data.*;
import www.pactera.com.coverage.project.component.data.execution.ExecutionData;
import www.pactera.com.coverage.project.component.data.session.SessionInfo;
import www.pactera.com.coverage.project.dao.objectDao.CoverageInfoDoMapper;
import www.pactera.com.coverage.project.dao.objectDao.SourceClassCoverageDOMapper;
import www.pactera.com.coverage.project.dao.objectDo.SourceClassCoverageDO;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ReportVisitorImpl implements IReportVisitor{

    private List<SessionInfo> sessionInfos;

    private Collection<ExecutionData> executionData;

    private CollectCoverageReqDTO coverageReqDTO;

    private String username;

    @Autowired
    private SourceClassCoverageDOMapper sourceClassCoverageDOMapper;


    @Override
    public void visitInfo(List<SessionInfo> sessionInfos, Collection<ExecutionData> executionData,CollectCoverageReqDTO collectCoverageReqDTO,String username) throws IOException {
        this.sessionInfos = sessionInfos;
        this.executionData = executionData;
        this.coverageReqDTO = collectCoverageReqDTO;
        this.username = username;
    }

    @Override
    public void visitEnd() throws IOException {

    }

    @Override
    public void visitBundle(IBundleCoverage bundle, ISourceFileLocator locator) throws IOException {
        // 从header里面取出userId  然后再获取到用户名
        for(IPackageCoverage packageCoverage : bundle.getPackages()){
            if(!packageCoverage.containsCode()){
                continue;
            }
            if(!packageCoverage.getSourceFiles().isEmpty()){
                String packageName =packageCoverage.getName();
                for(ISourceFileCoverage s : packageCoverage.getSourceFiles()){
                    if (!s.containsCode()) {
                        continue;
                    }
                    int coverLine = s.getLineCounter().getCoveredCount();
                    int totalCount  = s.getLineCounter().getTotalCount();
                    String sourceFileName = s.getName();
                    SourceClassCoverageDO sourceDO = new SourceClassCoverageDO();
                    sourceDO.setProjectName(coverageReqDTO.getProjectName());
                    sourceDO.setVersionNumber(coverageReqDTO.getVersionNumber());
                    sourceDO.setOperator(username);
                    sourceDO.setOperationTime(new Date());
                    sourceDO.setSourceName(sourceFileName);
                    sourceDO.setPackageName(packageName);
                    sourceDO.setCoverLineNumber(String.valueOf(coverLine));
                    sourceDO.setTotalLineNumber(String.valueOf(totalCount));
                    sourceDO.setSourceCoverageRate(StringUtils.coverConversion(coverLine,totalCount));
                    Reader reader = locator.getSourceFile("main/java/"+packageName,sourceFileName);
                    read(s,reader,sourceDO);
                }
            }else {

            }
        }
    }

    private void read(ISourceNode source,Reader contents,SourceClassCoverageDO sourceDO) throws IOException{
        ClassCover classCover = new ClassCover();
        BufferedReader lineBuffer = new BufferedReader(contents);
        String line;
        int nr = 0;
        while ((line = lineBuffer.readLine()) != null) {
            nr++;
            renderCodeLine(line, source.getLine(nr), nr,classCover);
        }
        List<Integer> codeCoverLine = classCover.getCodeCoverLine();
        List<Integer> missCoverLine = classCover.getMissCoverLine();
        List<Integer> notCoverLine = classCover.getNotCoverLine();
        sourceDO.setCoverLine(JSON.toJSONString(codeCoverLine));
        sourceDO.setMissLine(JSON.toJSONString(missCoverLine));
        sourceDO.setNotLine(JSON.toJSONString(notCoverLine));
        SourceClassCoverageDO classCoverageDO =sourceClassCoverageDOMapper.selectSourceClassCoverage(sourceDO.getProjectName(),sourceDO.getVersionNumber(),sourceDO.getOperator(),sourceDO.getPackageName(),sourceDO.getSourceName());
        if(null == classCoverageDO){
            sourceClassCoverageDOMapper.insert(sourceDO);
        }else {
            sourceClassCoverageDOMapper.update(sourceDO);
        }


    }

    private void renderCodeLine(String linesrc,ILine line,int lineNr,ClassCover classCover) throws IOException{
        final String style;
        switch (line.getStatus()) {
            case ICounter.NOT_COVERED:
                style = Styles.NOT_COVERED;
                classCover.getMissCoverLine().add(lineNr);
                break;
            case ICounter.FULLY_COVERED:
                style = Styles.FULLY_COVERED;
                classCover.getCodeCoverLine().add(lineNr);
                break;
            case ICounter.PARTLY_COVERED:
                style = Styles.PARTLY_COVERED;
                classCover.getCodeCoverLine().add(lineNr);
                break;
            default:
                classCover.getNotCoverLine().add(lineNr);
                break;
        }
    }
    @Override
    public IReportGroupVisitor visitGroup(String name) throws IOException {
        return null;
    }




}
