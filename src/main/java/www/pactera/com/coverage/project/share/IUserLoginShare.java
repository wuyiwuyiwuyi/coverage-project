package www.pactera.com.coverage.project.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.*;


import javax.validation.Valid;

@RequestMapping("/pactera")
public interface IUserLoginShare {

    @RequestMapping("/user/login")
    @ResponseBody
    ResponseData<UserLoginRespDTO> login(@RequestBody @Valid UserLoginReqDTO reqDTO);

    @RequestMapping("/user/register")
    @ResponseBody
    ResponseData<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterReqDTO req);

    @RequestMapping("/add/eru5st/user")
    @ResponseBody
    ResponseData<UserAddRespDTO> userAdd(@RequestBody @Valid UserAddReqDTO reqDTO);

    @RequestMapping("/queryAdd/yi4buf/user")
    @ResponseBody
    ResponseData<QueryUserAddRespDTO> selectAddAll(@RequestBody @Valid QueryUserAddReqDTO queryUserAddReqDTO);

    @RequestMapping("/updateStatus/mj6vdy/user")
    @ResponseBody
    ResponseData<UpdateUserStatusRespDTO> updateStatus(@RequestBody @Valid UpdateUserStatusReqDTO reqDTO);

    @RequestMapping("/deleteUser/nig7yu/user")
    @ResponseBody
    ResponseData<UserDeleteRespDTO> deleteUser(@RequestBody @Valid UserDeleteReqDTO reqDTO);



}
