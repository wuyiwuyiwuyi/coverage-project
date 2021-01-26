package www.pactera.com.coverage.project.share;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.UserLoginReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserLoginRespDTO;
import www.pactera.com.coverage.project.common.Dto.UserRegisterReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserRegisterRespDTO;


import javax.validation.Valid;

@RequestMapping("/pactera")
public interface IUserLoginShare {

    @RequestMapping("/user/login")
    @ResponseBody
    ResponseData<UserLoginRespDTO> login(@RequestBody @Valid UserLoginReqDTO reqDTO);

    @RequestMapping("/user/register")
    @ResponseBody
    ResponseData<UserRegisterRespDTO> register(@RequestBody @Valid UserRegisterReqDTO req);

}
