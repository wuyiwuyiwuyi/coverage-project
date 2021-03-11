package www.pactera.com.coverage.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import www.pactera.com.coverage.project.AccessControl.UserCommon.SecurityUser;
import www.pactera.com.coverage.project.common.Dto.UserDTO;
import www.pactera.com.coverage.project.dao.objectDao.LoginDOMapper;
import www.pactera.com.coverage.project.dao.objectDao.UserWhiteListDOMapper;
import www.pactera.com.coverage.project.dao.objectDo.LoginDO;
import www.pactera.com.coverage.project.dao.objectDo.UserWhiteListDO;

import java.util.List;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private LoginDOMapper loginDOMapper;

    @Autowired
    private UserWhiteListDOMapper userWhiteListDOMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LoginDO loginDO = loginDOMapper.selectByUsername(s);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(loginDO.getId());
        userDTO.setUsername(loginDO.getUsername());
        userDTO.setPassword(loginDO.getPassword());
        String role = loginDO.getRole();
        if(""==role || null ==role){
            role = "USER_ROLE";
        }
        userDTO.setRole(role);
        List<UserWhiteListDO> userWhiteListDOS = userWhiteListDOMapper.selectApprovalTask();
        int taskNum = userWhiteListDOS.isEmpty() || userWhiteListDOS.size() ==0?0:userWhiteListDOS.size();
        userDTO.setTask(taskNum);
        return new SecurityUser(userDTO);
    }
}
