package www.pactera.com.coverage.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.UserLoginReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserLoginRespDTO;
import www.pactera.com.coverage.project.common.Dto.UserRegisterReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserRegisterRespDTO;
import www.pactera.com.coverage.project.common.systemEmun.SystemMessageEnum;
import www.pactera.com.coverage.project.commonTools.JwtUtil;
import www.pactera.com.coverage.project.dao.objectDao.LoginDOMapper;
import www.pactera.com.coverage.project.dao.objectDo.LoginDO;
import www.pactera.com.coverage.project.service.UserLoginService;

import java.util.Date;


@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private LoginDOMapper loginDOMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


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
        LoginDO loginDO = new LoginDO();
        loginDO.setUsername(req.getUsername());
        loginDO.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        loginDO.setCreateTime(new Date());
        loginDOMapper.insert(loginDO);
        UserRegisterRespDTO userRegisterRespDTO = new UserRegisterRespDTO();
        userRegisterRespDTO.setResult(true);
        return new ResponseData<>(SystemMessageEnum.SUCCESS.getCode(), SystemMessageEnum.SUCCESS.getMsg(), userRegisterRespDTO);
    }



}
