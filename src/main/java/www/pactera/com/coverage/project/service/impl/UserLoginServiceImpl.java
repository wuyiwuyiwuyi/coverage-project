package www.pactera.com.coverage.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.*;
import www.pactera.com.coverage.project.common.systemEmun.SystemMessageEnum;
import www.pactera.com.coverage.project.commonTools.JwtUtil;
import www.pactera.com.coverage.project.dao.objectDao.LoginDOMapper;
import www.pactera.com.coverage.project.dao.objectDao.UserWhiteListDOMapper;
import www.pactera.com.coverage.project.dao.objectDo.LoginDO;
import www.pactera.com.coverage.project.dao.objectDo.UserWhiteListDO;
import www.pactera.com.coverage.project.service.UserLoginService;
import org.apache.commons.codec.binary.Base64;
import java.text.SimpleDateFormat;
import java.util.*;



@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Value("${user.white.switch}")
    private String user_white_switch;

    @Autowired
    private LoginDOMapper loginDOMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserWhiteListDOMapper userWhiteListDOMapper;


    @Override
    public ResponseData<UserLoginRespDTO> login(UserLoginReqDTO reqDTO) {

        String token = JwtUtil.sign(reqDTO.getUsername(), reqDTO.getPassword());
        UserLoginRespDTO resp = new UserLoginRespDTO();
        LoginDO loginDO = new LoginDO();
        loginDO.setUsername(reqDTO.getUsername());
        loginDO.setPassword(token);
        loginDO.setCreateTime(new Date());
        loginDOMapper.insert(loginDO);
        resp.setResult(true);
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), resp);
    }

    @Override
    public ResponseData<UserRegisterRespDTO> register(UserRegisterReqDTO req) {
        String username  = req.getUsername();
        LoginDO loginDO2 = loginDOMapper.selectByUsername(username);
        if(null != loginDO2){
            UserRegisterRespDTO userRegisterRespDTO = new UserRegisterRespDTO();
            userRegisterRespDTO.setResult(false);
            return new ResponseData<>(SystemMessageEnum.REGISTER_EXCEPTION.getCode(), SystemMessageEnum.REGISTER_EXCEPTION.getMsg(), userRegisterRespDTO);
        }
        if("Y".equals(user_white_switch)){
            UserWhiteListDO userWhiteListDO = userWhiteListDOMapper.selectOne(username);
            if(null == userWhiteListDO){
                UserRegisterRespDTO userRegisterRespDTO = new UserRegisterRespDTO();
                userRegisterRespDTO.setResult(false);
                return new ResponseData<>(SystemMessageEnum.ONT_USER_WHITE_LIST.getCode(), SystemMessageEnum.ONT_USER_WHITE_LIST.getMsg(), userRegisterRespDTO);
            }
        }
        LoginDO loginDO = new LoginDO();
        loginDO.setUsername(req.getUsername());
        loginDO.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        loginDO.setCreateTime(new Date());
        loginDOMapper.insert(loginDO);
        UserRegisterRespDTO userRegisterRespDTO = new UserRegisterRespDTO();
        userRegisterRespDTO.setResult(true);
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), userRegisterRespDTO);
    }

    @Override
    public ResponseData<UserAddRespDTO> userAdd(UserAddReqDTO reqDTO) {

        UserWhiteListDO users= userWhiteListDOMapper.selectOne(reqDTO.getUsername());
        if(null != users){
            return new ResponseData<>(SystemMessageEnum.REGISTER_EXCEPTION.getCode(),SystemMessageEnum.REGISTER_EXCEPTION.getMsg(),new UserAddRespDTO(false));
        }
        String username = reqDTO.getUsername();
        String password = reqDTO.getPassword();
        String role = reqDTO.getRole();
        UserWhiteListDO userWhiteListDO = new UserWhiteListDO();
        userWhiteListDO.setEmployeeId(username);
        userWhiteListDO.setPassword(getEncoder(password));
        userWhiteListDO.setRole(role);
        userWhiteListDO.setIsAuthorization("1"); //   0-拒绝 1-审批中 2-通过
        userWhiteListDO.setAuthorizationData(new Date());
        userWhiteListDO.setIsDelete("N");
        userWhiteListDOMapper.insert(userWhiteListDO);
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), new UserAddRespDTO(true));
    }

    @Override
    public ResponseData<QueryUserAddRespDTO> selectAddAll(QueryUserAddReqDTO queryUserAddReqDTO) {
        PageHelper.startPage(queryUserAddReqDTO.getCurrentPage(),queryUserAddReqDTO.getPageSize(),true);
        List<UserWhiteListDO> userWhiteListDOS = userWhiteListDOMapper.selectAll();
        //List<UserWhiteListDO> userWhiteListDOS2 = userWhiteListDOS.stream().sorted(Comparator.comparing(UserWhiteListDO::getAuthorizationData)).collect(Collectors.toList());
        if(!userWhiteListDOS.isEmpty() && userWhiteListDOS.size()>0){
            Collections.sort(userWhiteListDOS, new Comparator<UserWhiteListDO>() {
                @Override
                public int compare(UserWhiteListDO o1, UserWhiteListDO o2) {
                    if(o1.getAuthorizationData().before(o2.getAuthorizationData())){
                        return 1;
                    }else if(o1.getAuthorizationData().after(o2.getAuthorizationData())){
                        return -1;
                    }else {
                        return 0;
                    }
                }
            });
        }
        PageInfo<UserWhiteListDO> pageInfo = new PageInfo<>(userWhiteListDOS);
        List<QueryUserAddDTO> queryUserAddDTOS = new ArrayList<>();
        for(UserWhiteListDO userWhiteListDO : pageInfo.getList()){
            QueryUserAddDTO queryUserAddDTO = QueryUserAddDTO.builder()
                    .username(userWhiteListDO.getEmployeeId())
                    .password(getDecoder(userWhiteListDO.getPassword()))
                    .role(userWhiteListDO.getRole())
                    .status(userWhiteListDO.getIsAuthorization())
                    .authorizationData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userWhiteListDO.getAuthorizationData()))
                    .build();
            queryUserAddDTOS.add(queryUserAddDTO);
        }
        QueryUserAddRespDTO queryUserAddRespDTO = new QueryUserAddRespDTO();
        queryUserAddRespDTO.setCurrentPage(String.valueOf(pageInfo.getPageNum()));
        queryUserAddRespDTO.setHasNextPage(pageInfo.isHasNextPage());
        queryUserAddRespDTO.setPageSize(String.valueOf(pageInfo.getPageSize()));
        queryUserAddRespDTO.setTotalCount(String.valueOf(pageInfo.getTotal()));
        queryUserAddRespDTO.setTotalPage(String.valueOf(pageInfo.getPages()));
        queryUserAddRespDTO.setQueryUserAddDTOS(queryUserAddDTOS);
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), queryUserAddRespDTO);
    }

    @Override
    public ResponseData<UpdateUserStatusRespDTO> updateStatus(UpdateUserStatusReqDTO reqDTO) {
        String username = reqDTO.getUsername();
        String password = reqDTO.getPassword();
        String role = reqDTO.getRole();
        String status = reqDTO.getStatus();
        userWhiteListDOMapper.updateStatus(username,role,status);
        if("2".equals(status)){
            LoginDO loginDO = new LoginDO();
            loginDO.setUsername(username);
            loginDO.setPassword(bCryptPasswordEncoder.encode(password));
            loginDO.setCreateTime(new Date());
            loginDO.setRole(role);
            loginDO.setIsDelete("N");
            loginDOMapper.insert(loginDO);
        }
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(),new UpdateUserStatusRespDTO(true));
    }

    @Override
    public ResponseData<UserDeleteRespDTO> deleteUser(UserDeleteReqDTO reqDTO) {
       String username = reqDTO.getUsername();
       String password = reqDTO.getPassword();
       String status = reqDTO.getStatus();
       String role = reqDTO.getRole();
       if("admin".equals(role)){
            return new ResponseData<>(SystemMessageEnum.NOT_DELETE_ADMIN_USER.getCode(),SystemMessageEnum.NOT_DELETE_ADMIN_USER.getMsg(),new UserDeleteRespDTO(false));
       }
       String passwordEncoder = getEncoder(password);
       //String passwordEncoder = null == getEncoder(password)?password:getEncoder(password);
       userWhiteListDOMapper.deleteUser(username,passwordEncoder,role);
       loginDOMapper.deleteUser(username);
       return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(),SystemMessageEnum.SUCCESS.getMsg(),new UserDeleteRespDTO(true));
    }

    /**
     * 生成密钥
     * @return
     * @throws Exception
     */
    private static String getEncoder(String password){
        return new String(Base64.encodeBase64(password.getBytes()));
    }

    /**
     * 解密密钥
     * @return
     * @throws Exception
     */
    private String getDecoder(String passwordEncoder){
        return new String(Base64.decodeBase64(passwordEncoder.getBytes()));
    }

    public static void main(String[] args) {
        

    }




}
