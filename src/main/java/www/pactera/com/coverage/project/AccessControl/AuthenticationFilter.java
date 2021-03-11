package www.pactera.com.coverage.project.AccessControl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import www.pactera.com.coverage.project.AccessControl.UserCommon.SecurityUser;
import www.pactera.com.coverage.project.common.BaseEntity.ResponseData;
import www.pactera.com.coverage.project.common.Dto.UserLoginReqDTO;
import www.pactera.com.coverage.project.common.Dto.UserLoginRespDTO;
import www.pactera.com.coverage.project.common.systemEmun.SystemMessageEnum;


import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final static String CONTENT_TYPE = "application/json; charset=utf-8";

    private final static String CHARACTER_ENCODING = "UTF-8";

    private AuthenticationManager authenticationManager;


    @Resource
    private JwtProperties jwtProperties;

    public AuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/pactera/user/login");
    }

    /**
     * 验证用户身份。
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)  throws AuthenticationException {

        try {
            UserLoginReqDTO userLoginReqDTO = new ObjectMapper()
                    .readValue(request.getInputStream(),UserLoginReqDTO.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginReqDTO.getUsername(),
                            userLoginReqDTO.getPassword(),
                            new ArrayList<>())
            );
        }catch (IOException e ){
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户身份验证成功后调用的方法
     * @param request
     * @param response
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        SecurityUser jwtUser = (SecurityUser) auth.getPrincipal();
        String role = jwtUser.getRole();
        int num = jwtUser.getTask();
        String token  = JwtUtils.createToken(jwtUser.getUsername());
        response.setHeader("Authorization","Bearer "+token);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        ResponseData<UserLoginRespDTO> responseDs = new ResponseData<>(SystemMessageEnum.LOGIN_SUCCESS.getCode(),SystemMessageEnum.LOGIN_SUCCESS.getMsg(),new UserLoginRespDTO(true,role,num));
        String responseData = JSON.toJSONString(responseDs);
        response.getWriter().write(responseData);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException{
        ResponseData<UserLoginRespDTO> responseDs = new ResponseData<>(SystemMessageEnum.LOGIN_FAILURE.getCode(),SystemMessageEnum.LOGIN_FAILURE.getMsg(),new UserLoginRespDTO(false,"",0));
        String responseData = JSON.toJSONString(responseDs);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.getWriter().write(responseData);
    }

}
