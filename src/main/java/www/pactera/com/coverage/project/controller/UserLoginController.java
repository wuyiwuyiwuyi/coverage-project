package www.pactera.com.coverage.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.UserLoginReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserLoginRespDTO;
import www.pactera.com.coverage.project.common.Dto.UserRegisterReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserRegisterRespDTO;
import www.pactera.com.coverage.project.service.UserLoginService;
import www.pactera.com.coverage.project.share.IUserLoginShare;


import javax.validation.Valid;

@Controller
public class UserLoginController implements IUserLoginShare {

    @Autowired
    private UserLoginService userLoginService;

    @Override
    public ResponseData<UserLoginRespDTO> login(@Valid UserLoginReqDTO reqDTO) {
        //log.info("登陆请求开始，req={}",reqDTO);
        return userLoginService.login(reqDTO);
    }

    @Override
    public ResponseData<UserRegisterRespDTO> register(@Valid UserRegisterReqDTO req) {
        return userLoginService.register(req);
    }
}
