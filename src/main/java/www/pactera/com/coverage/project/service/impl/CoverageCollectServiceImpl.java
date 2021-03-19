package www.pactera.com.coverage.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import www.pactera.com.coverage.project.AccessControl.JwtUtils;
import www.pactera.com.coverage.project.common.BaseEntity.Data;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.*;
import www.pactera.com.coverage.project.common.systemEmun.SystemMessageEnum;
import www.pactera.com.coverage.project.commonTools.StringUtils;
import www.pactera.com.coverage.project.commonTools.fileCommonExecUtil;
import www.pactera.com.coverage.project.component.core.analysis.Analyzer;
import www.pactera.com.coverage.project.component.core.analysis.CoverageBuilder;
import www.pactera.com.coverage.project.component.core.tools.ExecFileLoader;
import www.pactera.com.coverage.project.component.data.IBundleCoverage;
import www.pactera.com.coverage.project.component.data.ISourceFileCoverage;
import www.pactera.com.coverage.project.component.data.TargetFile;
import www.pactera.com.coverage.project.component.data.execution.ExecutionDataWriter;
import www.pactera.com.coverage.project.component.report.DirectorySourceFileLocator;
import www.pactera.com.coverage.project.component.report.IMergeVisitor;
import www.pactera.com.coverage.project.component.report.IReportVisitor;
import www.pactera.com.coverage.project.component.runtime.remote.RemoteControlReader;
import www.pactera.com.coverage.project.component.runtime.remote.RemoteControlWriter;
import www.pactera.com.coverage.project.dao.objectDao.CoverageInfoDoMapper;
import www.pactera.com.coverage.project.dao.objectDao.PortRelevanceDOMapper;
import www.pactera.com.coverage.project.dao.objectDao.SourceClassCoverageDOMapper;
import www.pactera.com.coverage.project.dao.objectDao.SourcePathInfoDOMapper;
import www.pactera.com.coverage.project.dao.objectDo.CoverageInfoDO;
import www.pactera.com.coverage.project.dao.objectDo.PortRelevanceDO;
import www.pactera.com.coverage.project.dao.objectDo.SourceClassCoverageDO;
import www.pactera.com.coverage.project.dao.objectDo.SourcePathInfoDO;
import www.pactera.com.coverage.project.service.ICoverageCollectService;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoverageCollectServiceImpl implements ICoverageCollectService {


    @Value("${create.coverage.filePath}")
    private String exec_file_path;

    private final static String separator_flag = "/";

    private String base_file = separator_flag+"coverage.exec";

    private String merge_base_file = separator_flag+"merge.exec";

    @Autowired
    private CoverageInfoDoMapper coverageInfoDoMapper;

    @Autowired
    private SourceClassCoverageDOMapper sourceClassCoverageDOMapper;

    @Autowired
    private SourcePathInfoDOMapper sourcePathInfoDOMapper;

    @Autowired
    private PortRelevanceDOMapper portRelevanceDOMapper;

    @Autowired
    private IReportVisitor reportVisitor;

    @Autowired
    private IMergeVisitor mergeVisitor;


    @Override
    public ResponseData<CollectCoverageRespDTO> coverageCollect(CollectCoverageReqDTO reqDTO,String token) {
        String username = JwtUtils.getUsernameFromToken(token);
        String serverIp = reqDTO.getServerIp();
        String serverName = reqDTO.getProjectName();
        String serverVersion = reqDTO.getVersionNumber();
        if(StringUtils.isEmpty(serverIp) || StringUtils.isEmpty(serverName) || StringUtils.isEmpty(serverVersion)){
            return new ResponseData<>(SystemMessageEnum.PARAMETER_LACK.getCode(),SystemMessageEnum.PARAMETER_LACK.getMsg(),new CollectCoverageRespDTO(false));
        }
        //根据服务名称+版本号生成此次收据的文件夹
        String basePath = fileCommonExecUtil.JointPath(exec_file_path,serverName,serverVersion,separator_flag);
        fileCommonExecUtil.createFile(basePath);
        String baseSourcePath = fileCommonExecUtil.JointPath(exec_file_path,"source",serverName,separator_flag);
        File sourceFile =  new File(baseSourcePath);
        if(!sourceFile.exists()){
            return new ResponseData<>(SystemMessageEnum.SOURCES_FILE_NOT_FIND.getCode(),SystemMessageEnum.SOURCES_FILE_NOT_FIND.getMsg(),new CollectCoverageRespDTO(false));
        }
        try {
            //复制保存项目源码
            fileCommonExecUtil.copyFolder(baseSourcePath,basePath+separator_flag+"source");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //查询数据库配置的项目端口号
        PortRelevanceDO portRelevanceDO = portRelevanceDOMapper.selectPort(serverName);
        String projectPort  = portRelevanceDO.getPort();
        if(StringUtils.isEmpty(projectPort) || projectPort.length()<4){
            return new ResponseData<>(SystemMessageEnum.PORT_RELEVANCE_NOT_FIND.getCode(),SystemMessageEnum.PORT_RELEVANCE_NOT_FIND.getMsg(),new CollectCoverageRespDTO(false));
        }
        int port = Integer.parseInt(projectPort);
        try {
            FileOutputStream localFile = new FileOutputStream(basePath+base_file);
            ExecutionDataWriter localWriter = new ExecutionDataWriter(localFile);
            Socket socket = new Socket(InetAddress.getByName(serverIp),port);
            RemoteControlWriter writer = new RemoteControlWriter(socket.getOutputStream());
            RemoteControlReader reader = new RemoteControlReader(socket.getInputStream());
            reader.setSessionInfoVisitor(localWriter);
            reader.setExecutionDataVisitor(localWriter);
            writer.visitDumpCommand(true,false);
            if (!reader.read()) {
                return new ResponseData<>(SystemMessageEnum.DATA_READER_EXCEPTION.getCode(),SystemMessageEnum.DATA_READER_EXCEPTION.getMsg(),new CollectCoverageRespDTO(false));
            }
            socket.close();
            localFile.close();
        }catch (IOException e){
            e.printStackTrace();
            return new ResponseData<>(SystemMessageEnum.CONNECTION_EXCEPTION.getCode(),SystemMessageEnum.CONNECTION_EXCEPTION.getMsg(),new CollectCoverageRespDTO(false));
        }
        TargetFile targetFile = new TargetFile(new File(basePath),separator_flag);
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        ExecFileLoader execFileLoader = new ExecFileLoader();
        try {
            execFileLoader.load(targetFile.getExecutionDataFile());
            Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(),coverageBuilder);
            analyzer.analyzeAll(targetFile.getClassesDirectory());
            IBundleCoverage bundleCoverage = coverageBuilder.getBundle("pactera");
            recordSourceInfo(coverageBuilder,reqDTO,username);
            recordCoverageInfo(reqDTO,bundleCoverage,username);
            reportVisitor.visitInfo(execFileLoader.getSessionInfoStore().getInfos(),
                    execFileLoader.getExecutionDataStore().getContents(),reqDTO,username);
            reportVisitor.visitBundle(bundleCoverage,
                    new DirectorySourceFileLocator(targetFile.getSourceDirectory(), "utf-8", 4));
            coverageInfoDoMapper.updateStatus(reqDTO.getProjectName(),reqDTO.getVersionNumber(),username,"成功");
        } catch (IOException e) {
            coverageInfoDoMapper.updateStatus(reqDTO.getProjectName(),reqDTO.getVersionNumber(),username,"失败");
            return new ResponseData<>(SystemMessageEnum.EXCEPTION.getCode(), SystemMessageEnum.EXCEPTION.getMsg(), new CollectCoverageRespDTO(false));
        }
        CollectCoverageRespDTO co = new CollectCoverageRespDTO();
        co.setResult(true);
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), co);
    }


    @Override
    public ResponseData<MergeCoverageRespDTO> coverageMerge(MergeCoverageReqDTO reqDTO,String token) {
        String username = JwtUtils.getUsernameFromToken(token);
        //对请求合并的版本进行校验   1：对是否为同一个用户进行校验   2：对版本号是否为同一迭代里面的大版本进行校验（主要保证合并的是否所属的各版本）
        List<ProjectBaseInfo> reqList = reqDTO.getMergeServerInfo();
        //Map<String,List<ProjectBaseInfo>> handleUserMap = reqList.stream().collect(Collectors.groupingBy(ProjectBaseInfo::getOperator));
        Map<String,List<ProjectBaseInfo>> projectNameMap = reqList.stream().collect(Collectors.groupingBy(ProjectBaseInfo::getProjectName));
        if(projectNameMap.entrySet().size()>1){
            return new ResponseData<>(SystemMessageEnum.NOT_SAME_PROJECT_NAME.getCode(),SystemMessageEnum.NOT_SAME_PROJECT_NAME.getMsg(), new MergeCoverageRespDTO(false));
        }
        //2: 校验版本号
        ExecFileLoader loader = new ExecFileLoader();
        List<File> execFiles = new ArrayList<File>();
        //合并
        for(ProjectBaseInfo projectBaseInfo : reqList){
            String basePath = fileCommonExecUtil.JointPath(exec_file_path,projectBaseInfo.getProjectName(),projectBaseInfo.getVersionNumber(),separator_flag);
            execFiles.add(new File(basePath+base_file));
        }
        if(execFiles.isEmpty() || execFiles.size()<2){
            return new ResponseData<>(SystemMessageEnum.ONT_GREATER_PROJECT.getCode(),SystemMessageEnum.ONT_GREATER_PROJECT.getMsg(), new MergeCoverageRespDTO(false));
        }
        String mergeVersion = "";
        String baseMergePath = "";
        List<String> projectNameList = new ArrayList<>();
        try {
            for(File execFile :execFiles){
                loader.load(execFile);
            }
            for(ProjectBaseInfo projectBaseInfo : reqList){
                projectNameList.add(projectBaseInfo.getVersionNumber());
            }
            Collections.sort(projectNameList);
            for(int i =0;i<projectNameList.size();i++){
                if(i<projectNameList.size()-1){
                    mergeVersion += projectNameList.get(i)+"-";
                }else {
                    mergeVersion += projectNameList.get(i);
                }
            }
            baseMergePath = fileCommonExecUtil.JointPath(exec_file_path,reqList.get(0).getProjectName(),mergeVersion,separator_flag);
            fileCommonExecUtil.createFile(baseMergePath);
            File destFile = new File(baseMergePath+merge_base_file);
            loader.save(destFile, true);
            if(!destFile.exists()){
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseData<>(SystemMessageEnum.ANALYSIS_FILE_EXCEPTION.getCode(),SystemMessageEnum.ANALYSIS_FILE_EXCEPTION.getMsg(),new MergeCoverageRespDTO(false) );
        }
        try {
            String baseSourcePath = fileCommonExecUtil.JointPath(exec_file_path,reqList.get(0).getProjectName(),reqList.get(0).getVersionNumber(),separator_flag);
            fileCommonExecUtil.copyFolder(baseSourcePath+separator_flag+"source",baseMergePath+separator_flag+"source");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //解析merge.exec文件
        TargetFile targetFile = new TargetFile(new File(baseMergePath),merge_base_file,separator_flag);
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        ExecFileLoader execFileLoader = new ExecFileLoader();
        try {
            execFileLoader.load(targetFile.getMergeDataFile());
            Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(),coverageBuilder);
            analyzer.analyzeAll(targetFile.getClassesDirectory());
            IBundleCoverage bundleCoverage = coverageBuilder.getBundle("pactera");

            //String versionNumber = "merge-"+reqList.get(0).getVersionNum().substring(0,reqList.get(0).getVersionNum().indexOf("."))+"."+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            recordMergeCoverageInfo(reqList.get(0).getProjectName(),mergeVersion,bundleCoverage,username);
            recordSourceMergeInfo(coverageBuilder,mergeVersion,reqList.get(0).getProjectName(),username);
            mergeVisitor.visitInfo(execFileLoader.getSessionInfoStore().getInfos(),execFileLoader.getExecutionDataStore().getContents(),
                    reqList.get(0).getProjectName(),mergeVersion,username);
            mergeVisitor.visitBundle(bundleCoverage,
                    new DirectorySourceFileLocator(targetFile.getSourceDirectory(), "utf-8", 4));
            coverageInfoDoMapper.updateStatus(reqList.get(0).getProjectName(),mergeVersion,username,"成功");
        } catch (IOException e) {
            coverageInfoDoMapper.updateStatus(reqList.get(0).getProjectName(),mergeVersion,username,"失败");
            return new ResponseData<>(SystemMessageEnum.EXCEPTION.getCode(), SystemMessageEnum.EXCEPTION.getMsg(), new MergeCoverageRespDTO(false));
        }
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(),SystemMessageEnum.SUCCESS.getMsg(), new MergeCoverageRespDTO(true));
    }

    @Override
    public ResponseData<QueryCoverageRespDTO> coverageQuery(QueryCollectCoverageReqDTO reqDTO) {
        String projectName  = reqDTO.getProjectName();
        String versionNumber = reqDTO.getVersionNumber();
        String handleUser = reqDTO.getOperator();
        String status = reqDTO.getStatus();
        Date startDate = reqDTO.getStartTime();
        Date endDate = reqDTO.getEndTime();
        PageHelper.startPage(reqDTO.getCurrentPage(),reqDTO.getPageSize(),true);
        List<CoverageInfoDO>  coverageInfoDOS = coverageInfoDoMapper.selectCoverageInfoDOLists(projectName,versionNumber,handleUser,startDate,endDate,status);
        if(!coverageInfoDOS.isEmpty() && coverageInfoDOS.size()>0){
            Collections.sort(coverageInfoDOS, new Comparator<CoverageInfoDO>() {
                @Override
                public int compare(CoverageInfoDO o1, CoverageInfoDO o2) {
                    if(o1.getOperationTime().before(o2.getOperationTime())){
                        return 1;
                    }else if(o1.getOperationTime().after(o2.getOperationTime())){
                        return -1;
                    }else {
                        return 0;
                    }
                }
            });
        }
        PageInfo<CoverageInfoDO> pageInfo = new PageInfo<>(coverageInfoDOS);
        List<CoverageResultInfo> coverageResultInfos = new ArrayList<>();
        for(CoverageInfoDO coverageInfoDO : pageInfo.getList()){
            CoverageResultInfo coverageResultInfo = CoverageResultInfo.builder()
                    .projectName(coverageInfoDO.getProjectName())
                    .versionNumber(coverageInfoDO.getVersionNumber())
                    .phase(coverageInfoDO.getPhase())
                    .status(coverageInfoDO.getStatus())
                    .time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(coverageInfoDO.getOperationTime()))
                    .totalLineNumber(coverageInfoDO.getTotalLine())
                    .coverageRate(coverageInfoDO.getCoverageRate())
                    .operator(coverageInfoDO.getOperator())
                    .build();
            coverageResultInfos.add(coverageResultInfo);
        }
        QueryCoverageRespDTO queryCoverageRespDTO = new QueryCoverageRespDTO();
        queryCoverageRespDTO.setCoverageResultInfos(coverageResultInfos);
        queryCoverageRespDTO.setCurrentPage(String.valueOf(pageInfo.getPageNum()));
        queryCoverageRespDTO.setPageSize(String.valueOf(pageInfo.getPageSize()));
        queryCoverageRespDTO.setTotalCount(String.valueOf(pageInfo.getTotal()));
        queryCoverageRespDTO.setTotalPage(String.valueOf(pageInfo.getPages()));
        queryCoverageRespDTO.setHasNextPage(pageInfo.isHasNextPage());
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), queryCoverageRespDTO);
    }


    @Override
    public ResponseData<ProjectCoverageRespDTO> queryProjectCoverage(ProjectCoverageReqDTO projectCoverageReqDTO) {
        String projectName = projectCoverageReqDTO.getProjectName();
        String versionNumber = projectCoverageReqDTO.getVersionNumber();
        String operator = projectCoverageReqDTO.getOperator();
        if(StringUtils.isEmpty(projectName)
                || StringUtils.isEmpty(versionNumber)
                || StringUtils.isEmpty(operator)){
            return new ResponseData<>(SystemMessageEnum.PARAMETER_LACK_QUERY_COVERAGE.getCode(),SystemMessageEnum.PARAMETER_LACK_QUERY_COVERAGE.getMsg(), null);
        }
        List<SourcePathInfoDO> sourcePathInfoDOList = sourcePathInfoDOMapper.selectProjectFile(projectName,versionNumber,operator);
        System.out.println(sourcePathInfoDOList.size());
        Map<String,Data> dataMap = new HashMap<>();
        for (SourcePathInfoDO sourcePathInfoDO: sourcePathInfoDOList) {
            String packagePath = sourcePathInfoDO.getPackagePath();
            String fileName = packagePath.substring(packagePath.lastIndexOf("/")+1,packagePath.length());
            Data data = new Data();
            data.setCoverageLine(sourcePathInfoDO.getCoverLine());
            data.setMissLine(sourcePathInfoDO.getMissLine());
            data.setTotalLine(sourcePathInfoDO.getTotalLineNumber());
            String sourceCoverageRate = "0".equals(sourcePathInfoDO.getCoverLine())?"0%":sourcePathInfoDO.getSourceCoverageRate();
            data.setCoverageRate(sourceCoverageRate);
            dataMap.put(fileName,data);
        }
        CoverageInfoDO coverageInfoDO =coverageInfoDoMapper.selectProjectCoverage(projectName,versionNumber,operator);
        ProjectCoverageRespDTO projectCoverageRespDTO = ProjectCoverageRespDTO.builder()
               .projectName(coverageInfoDO.getProjectName())
               .versionNumber(coverageInfoDO.getVersionNumber())
               .operator(coverageInfoDO.getOperator())
               .phase(coverageInfoDO.getPhase())
               .status(coverageInfoDO.getStatus())
               .type(coverageInfoDO.getType())
               .operationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(coverageInfoDO.getOperationTime()))
               .totalLine(coverageInfoDO.getTotalLine())
               .coverageLine(coverageInfoDO.getCoverageLine())
               .coverageRate(coverageInfoDO.getCoverageRate())
               .data(buildProjectStructure3(sourcePathInfoDOList))
               .coverageMap(dataMap)
               .build();
        System.out.println("down");
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), projectCoverageRespDTO);
    }

    @Override
    public ResponseData<SourceCoverageRespDTO> querySourceCoverage(SourceCoverageReqDTO sourceCoverageReqDTO) {
        String projectName = sourceCoverageReqDTO.getProjectName();
        String versionName = sourceCoverageReqDTO.getVersionNumber();
        String operator = sourceCoverageReqDTO.getOperator();
        String sourceName = sourceCoverageReqDTO.getSourceName();
        if(StringUtils.isEmpty(projectName) || StringUtils.isEmpty(versionName)
                ||StringUtils.isEmpty(operator)
                || StringUtils.isEmpty(sourceName)){
            return new ResponseData<>(SystemMessageEnum.PARAMETER_LACK_QUERY_COVERAGE.getCode(),SystemMessageEnum.PARAMETER_LACK_QUERY_COVERAGE.getMsg(), null);
        }
        System.out.println(sourceCoverageReqDTO);
        String packageName  = sourcePathInfoDOMapper.selectPackageName(projectName,versionName,operator,sourceName);
        String sourcePath = packageName.substring(0,packageName.lastIndexOf("/"));
        SourceClassCoverageDO classCoverageDO = sourceClassCoverageDOMapper.selectSourceClassCoverage(projectName,versionName,operator,sourcePath,sourceName);
        if(null == classCoverageDO){
            return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(),new SourceCoverageRespDTO());
        }
        SourceCoverageRespDTO sourceCoverageRespDTO = SourceCoverageRespDTO.builder()
                .projectName(classCoverageDO.getProjectName())
                .versionNumber(classCoverageDO.getVersionNumber())
                .operator(classCoverageDO.getOperator())
                .operationTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(classCoverageDO.getOperationTime()))
                .packageName(classCoverageDO.getPackageName())
                .sourceName(classCoverageDO.getSourceName())
                .coverLine(classCoverageDO.getCoverLine())
                .missLine(classCoverageDO.getMissLine())
                .notLine(classCoverageDO.getNotLine())
                .coverLineNumber(classCoverageDO.getCoverLineNumber())
                .totalLineNumber(classCoverageDO.getTotalLineNumber())
                .sourceCoverageRate(classCoverageDO.getSourceCoverageRate())
                .build();
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), sourceCoverageRespDTO);
    }

    @Override
    public ResponseData<SourcesGainDTO> gainSourceFile(SourceCoverageReqDTO reqDTO) {
        String projectName = reqDTO.getProjectName();
        String versionName = reqDTO.getVersionNumber();
        String operator = reqDTO.getOperator();
        String sourceName = reqDTO.getSourceName();
        String sourcePackageName  = sourcePathInfoDOMapper.selectPackageName(projectName,versionName,operator,sourceName);
        String sourceFilePath = sourcePackageName.substring(0,sourcePackageName.lastIndexOf("/"));
        String packageName  = sourceFilePath;
        String basePath = fileCommonExecUtil.JointPath(exec_file_path,projectName,versionName,separator_flag);
        String packagePath = packageName.replace("/",separator_flag);
        String sourcePath = basePath+separator_flag+"source"+separator_flag+"src"+separator_flag+"main"+separator_flag+"java"+separator_flag+packagePath+separator_flag+sourceName;
        List<String> list = new ArrayList<>();
        File file = new File(sourcePath);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            LineNumberReader reader = new LineNumberReader(fileReader);
            String txt = "";
            int lines = 0;
            while (txt != null) {
                lines++;
                txt = reader.readLine();
                if(txt != null){
                    list.add(txt);
                }
            }
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SourcesGainDTO sourcesGainDTO = new SourcesGainDTO();
        sourcesGainDTO.setSourceResult(list);
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(),SystemMessageEnum.SUCCESS.getMsg(),sourcesGainDTO);

    }


    @Async
    public void recordCoverageInfo(CollectCoverageReqDTO reqDTO,IBundleCoverage bundleCoverage,String username){
        CoverageInfoDO coverageInfoDO = new CoverageInfoDO();
        coverageInfoDO.setProjectName(reqDTO.getProjectName());
        coverageInfoDO.setVersionNumber(reqDTO.getVersionNumber());
        coverageInfoDO.setPhase("sit");
        coverageInfoDO.setStatus("执行中");
        coverageInfoDO.setType("全量");
        coverageInfoDO.setCoverageLine(String.valueOf(bundleCoverage.getLineCounter().getCoveredCount()));
        coverageInfoDO.setTotalLine(String.valueOf(bundleCoverage.getLineCounter().getTotalCount()));
        coverageInfoDO.setCoverageRate(StringUtils.coverConversion(bundleCoverage.getLineCounter().getCoveredCount(),bundleCoverage.getLineCounter().getTotalCount()));
        coverageInfoDO.setOperator(username);
        coverageInfoDO.setOperationTime(new Date());
        coverageInfoDO.setIsDelete("N");
        coverageInfoDO.setIsMerge("N");
        CoverageInfoDO coverageDO =coverageInfoDoMapper.selectProjectCoverage(coverageInfoDO.getProjectName(),coverageInfoDO.getVersionNumber(),coverageInfoDO.getOperator());
        if(null == coverageDO){
            coverageInfoDoMapper.insert(coverageInfoDO);
        }else {
            coverageInfoDoMapper.update(coverageInfoDO);
        }
    }

    @Async
    public void recordMergeCoverageInfo(String projectName,String versionNumber,IBundleCoverage bundleCoverage,String username){
        CoverageInfoDO coverageInfoDO = new CoverageInfoDO();
        coverageInfoDO.setProjectName(projectName);
        coverageInfoDO.setVersionNumber(versionNumber);
        coverageInfoDO.setPhase("sit");
        coverageInfoDO.setStatus("执行中");
        coverageInfoDO.setType("全量");
        coverageInfoDO.setCoverageLine(String.valueOf(bundleCoverage.getLineCounter().getCoveredCount()));
        coverageInfoDO.setTotalLine(String.valueOf(bundleCoverage.getLineCounter().getTotalCount()));
        coverageInfoDO.setCoverageRate(StringUtils.coverConversion(bundleCoverage.getLineCounter().getCoveredCount(),bundleCoverage.getLineCounter().getTotalCount()));
        coverageInfoDO.setOperator(username);
        coverageInfoDO.setOperationTime(new Date());
        coverageInfoDO.setIsDelete("N");
        coverageInfoDO.setIsMerge("Y");
        CoverageInfoDO coverageDO =coverageInfoDoMapper.selectProjectCoverage(coverageInfoDO.getProjectName(),coverageInfoDO.getVersionNumber(),coverageInfoDO.getOperator());
        if(null == coverageDO){
            coverageInfoDoMapper.insert(coverageInfoDO);
        }else {
            coverageInfoDoMapper.update(coverageInfoDO);
        }
    }

    public void recordSourceMergeInfo(CoverageBuilder coverageBuilder,String version,String project,String username){
        Collection<ISourceFileCoverage> collections = coverageBuilder.getSourceFiles();
        List<SourcePathInfoDO> sourcePathInfoDOList = sourcePathInfoDOMapper.selectProjectFile(project,version,username);
        if(sourcePathInfoDOList == null || sourcePathInfoDOList.isEmpty()){
            for(ISourceFileCoverage sourceFileCoverage : collections){
                SourcePathInfoDO sourcePathInfoDO = new SourcePathInfoDO();
                sourcePathInfoDO.setProjectName(project);
                sourcePathInfoDO.setVersionNumber(version);
                sourcePathInfoDO.setPackagePath(sourceFileCoverage.getPackageName()+"/"+sourceFileCoverage.getName());
                sourcePathInfoDO.setCoverLine(String.valueOf(sourceFileCoverage.getLineCounter().getCoveredCount()));
                sourcePathInfoDO.setMissLine(String.valueOf(sourceFileCoverage.getLineCounter().getMissedCount()));
                sourcePathInfoDO.setTotalLineNumber(String.valueOf(sourceFileCoverage.getLineCounter().getTotalCount()));
                sourcePathInfoDO.setSourceCoverageRate(StringUtils.coverConversion(sourceFileCoverage.getLineCounter().getCoveredCount(),sourceFileCoverage.getLineCounter().getTotalCount()));
                sourcePathInfoDO.setOperator(username);
                sourcePathInfoDO.setOperationTime(new Date());
                sourcePathInfoDO.setIsDelete("N");
                sourcePathInfoDOMapper.insert(sourcePathInfoDO);
            }
        }


    }


    public void recordSourceInfo(CoverageBuilder coverageBuilder,CollectCoverageReqDTO reqDTO,String username){
        String projectName =  reqDTO.getProjectName();
        String versionNumber = reqDTO.getVersionNumber();
        Collection<ISourceFileCoverage> collections = coverageBuilder.getSourceFiles();
        List<SourcePathInfoDO> sourcePathInfoDOList = sourcePathInfoDOMapper.selectProjectFile(projectName,versionNumber,username);
        if(sourcePathInfoDOList == null || sourcePathInfoDOList.isEmpty()){
            for(ISourceFileCoverage sourceFileCoverage : collections){
                SourcePathInfoDO sourcePathInfoDO = new SourcePathInfoDO();
                sourcePathInfoDO.setProjectName(projectName);
                sourcePathInfoDO.setVersionNumber(versionNumber);
                sourcePathInfoDO.setPackagePath(sourceFileCoverage.getPackageName()+"/"+sourceFileCoverage.getName());
                sourcePathInfoDO.setCoverLine(String.valueOf(sourceFileCoverage.getLineCounter().getCoveredCount()));
                sourcePathInfoDO.setMissLine(String.valueOf(sourceFileCoverage.getLineCounter().getMissedCount()));
                sourcePathInfoDO.setTotalLineNumber(String.valueOf(sourceFileCoverage.getLineCounter().getTotalCount()));
                sourcePathInfoDO.setSourceCoverageRate(StringUtils.coverConversion(sourceFileCoverage.getLineCounter().getCoveredCount(),sourceFileCoverage.getLineCounter().getTotalCount()));
                sourcePathInfoDO.setOperator(username);
                sourcePathInfoDO.setOperationTime(new Date());
                sourcePathInfoDO.setIsDelete("N");
                sourcePathInfoDOMapper.insert(sourcePathInfoDO);
            }
        }
    }


    /*public Data buildProjectStructure(ProjectCoverageReqDTO projectCoverageReqDTO){
        return null;
        *//*String projectName = projectCoverageReqDTO.getProjectName();
        String versionNumber = projectCoverageReqDTO.getVersionNumber();
        String operator = projectCoverageReqDTO.getOperator();
        List<SourcePathInfoDO> sourcePathInfoDOList = sourcePathInfoDOMapper.selectProjectFile(projectName,versionNumber,operator);
        if(sourcePathInfoDOList.isEmpty() || sourcePathInfoDOList.size() ==0){
            return new Data();
        }
        Data<SourceTree> data = new Data();
        SourceTree[] source = new SourceTree[sourcePathInfoDOList.size()];
        for(int j=0;j<sourcePathInfoDOList.size();j++){
            String[] strings = sourcePathInfoDOList.get(j).getPackagePath().split("/");
            SourceTree sourceTree = new SourceTree();
            for(int i=strings.length-1;i>=0;i--){
                String packName =  strings[i];
                if(i==strings.length-1){
                    SourceTree sourceFileName2 = new SourceTree();
                    sourceFileName2.setLabel(packName);
                    sourceTree = sourceFileName2;
                }else {
                    SourceTree sourceFileName3 = new SourceTree();
                    sourceFileName3.setLabel(packName);
                    SourceTree[] fileNames = new SourceTree[1];
                    fileNames[0] = sourceTree;
                    sourceFileName3.setChildren(fileNames);
                    sourceTree = sourceFileName3;
                }
            }
            source[j] = sourceTree;
        }
        data.setData(source);
        //buildProjectStructure2(data);
        return data;*//*
    }*/

    public List<Map<String,Object>> buildProjectStructure3(List<SourcePathInfoDO> sourcePathInfoDOList){
        List<Map<String,String>> pathAllList  = new ArrayList<>();
        for (int i = 0; i<sourcePathInfoDOList.size(); i++ ) {
            Map<String,String> map = new HashMap<>();
            map.put("user_real_path",sourcePathInfoDOList.get(i).getPackagePath());
            pathAllList.add(map);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startPath", generateData(pathAllList));
        HashMap<String, Object> mapObject  = (HashMap<String, Object>)jsonObject.get("startPath");
        List<Map<String,Object>> object  = (List<Map<String,Object>>)mapObject.get("children");
        return object;
    }

    public HashMap<String, Object> generateData(List<Map<String, String>> mapList) {
        HashMap<String, Object> root = new HashMap<>(8);
        root.put("label", "start");
        ArrayList<String> arrayList = new ArrayList<>();
        root.put("children", arrayList);
        String onePath = "";
        String oneFileId = "";
        for (int i = 0; i < mapList.size(); i++) {
            oneFileId = mapList.get(i).get("file_id");
            onePath = zhuanYi(mapList.get(i).get("user_real_path"));

            addPath(root, onePath, oneFileId);
        }
        return root;
    }

    public void addPath(HashMap<String, Object> root, String path, String fileId) {
        String url = "";
        StringBuffer urlBuffer = new StringBuffer();
        if (path.charAt(0) == (char) '/') {
            path = path.substring(1, path.length());
        }
        String[] pathArr = path.split("/");
        for (int i = 0; i < pathArr.length; i++) {
            String name = pathArr[i];
            if (i == 0) {
                urlBuffer.append(name);
            } else {
                url += "/" + name;
                urlBuffer.append("/").append(name);
            }
            url = urlBuffer.toString();

            boolean flag = true;
            for (HashMap<String, Object> node : (ArrayList<HashMap<String, Object>>) root.get("children")) {

                if (node.get("label").equals(name)) {
                    root = node;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                HashMap<String, Object> newNode = new HashMap<>(8);
                newNode.put("label", name);
                newNode.put("children", new ArrayList<HashMap<String, Object>>());
                ((ArrayList<HashMap<String, Object>>) root.get("children")).add(newNode);
                root = newNode;
            }
        }
    }


    public String zhuanYi(String path) {
        //将双引号去掉，将多余的空字符去掉
        path = path.replaceAll("\"", "").replaceAll(" ", "");
        if (path.contains("\\\\")) {
            path = path.replaceAll("\\\\", "/");
        }
        if (path.contains("\\")) {
            path = path.replaceAll("\\\\", "/");
        }
        if (path.contains("//")) {
            path = path.replaceAll("//", "/");
        }
        if (path.contains("//")) {
            path = path.replaceAll("//", "/");
        }
        return path;
    }


    public String buildProjectStructure2(List<SourcePathInfoDO> sourcePathInfoDOList){
     /*   String projectName = projectCoverageReqDTO.getProjectName();
        String versionNumber = projectCoverageReqDTO.getVersionNumber();
        String operator = projectCoverageReqDTO.getOperator();
        List<SourcePathInfoDO> sourcePathInfoDOList = sourcePathInfoDOMapper.selectProjectFile(projectName,versionNumber,operator);
        if(sourcePathInfoDOList.isEmpty() || sourcePathInfoDOList.size() ==0){
            return "";
        }*/

        List<Map<String, String>> list=new ArrayList<Map<String,String>>();
        Map<String, String> item=new HashMap<String, String>();

        for(SourcePathInfoDO str:sourcePathInfoDOList){
            String strArr[]=str.getPackagePath().split("/");
            for(int i=0;i<strArr.length;i++){
                item=new HashMap<String, String>();
                if(i==0){
                    item.put("label", strArr[i]);
                    item.put("parentId", "");
                }else{
                    item.put("label", strArr[i]);
                    item.put("parentId", strArr[i-1]);
                }
                // 判断是否已经存在值相同的
                boolean isAdd=true;
                for(Map<String, String> itemT:list){
                    if(itemT.get("label").equals(item.get("label"))&&itemT.get("parentId").equals(item.get("parentId"))){
                        isAdd=false;
                    }
                }
                if(isAdd){
                    list.add(item);
                }
            }
        }
        // 开始递归
        JsonArray jsonArray=new JsonArray();
        for(Map<String, String> itemT:list){
            if("".equals(itemT.get("parentId"))){
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("label", itemT.get("label"));
                jsonArray.add(jsonObject);
                seracherItem(jsonObject,list);
            }
        }
        System.out.println(jsonArray.toString());
        return jsonArray.toString();

    }
    private static long count = 0;

    public void seracherItem(JsonObject jsonObject,List<Map<String, String>> list){
        //System.out.println(count++);
        JsonArray jsonArray=new JsonArray();
        try{
            jsonObject.add("children",jsonArray);
            for(Map<String, String> itemT:list){
                if(jsonObject.get("label").getAsString().equals(itemT.get("parentId"))){
                    //System.out.println(jsonObject.get("label").getAsString()+"  "+itemT.get("parentId"));
                    JsonObject jsonObjectT=new JsonObject();
                    String labelValue  = null == itemT?"":itemT.get("label");
                    jsonObjectT.addProperty("label", labelValue);
                    jsonArray.add(jsonObjectT);
                    seracherItem(jsonObjectT,list);
                }

            }
        }catch (Exception e){
            System.out.println(jsonObject);
            throw new NullPointerException();
        }
    }

}
