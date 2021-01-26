package www.pactera.com.coverage.project.service;


import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.UserLoginReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserLoginRespDTO;
import www.pactera.com.coverage.project.common.Dto.UserRegisterReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserRegisterRespDTO;

public interface UserLoginService {

    ResponseData<UserLoginRespDTO> login(UserLoginReqDTO reqDTO);

    ResponseData<UserRegisterRespDTO> register(UserRegisterReqDTO req);
}
