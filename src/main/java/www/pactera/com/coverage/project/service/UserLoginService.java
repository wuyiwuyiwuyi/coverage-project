package www.pactera.com.coverage.project.service;


import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.*;

public interface UserLoginService {

    ResponseData<UserLoginRespDTO> login(UserLoginReqDTO reqDTO);

    ResponseData<UserRegisterRespDTO> register(UserRegisterReqDTO req);

    ResponseData<UserAddRespDTO> userAdd(UserAddReqDTO reqDTO);

    ResponseData<QueryUserAddRespDTO> selectAddAll(QueryUserAddReqDTO queryUserAddReqDTO);

    ResponseData<UpdateUserStatusRespDTO> updateStatus(UpdateUserStatusReqDTO reqDTO);

    ResponseData<UserDeleteRespDTO> deleteUser(UserDeleteReqDTO reqDTO);

}
