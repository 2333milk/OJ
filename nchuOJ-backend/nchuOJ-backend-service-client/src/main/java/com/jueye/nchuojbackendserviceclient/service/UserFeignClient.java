package com.jueye.nchuojbackendserviceclient.service;

import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendmodel.model.entity.User;
import com.jueye.nchuojbackendmodel.model.enums.UserRoleEnum;
import com.jueye.nchuojbackendmodel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.jueye.nchuojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 
 */
@FeignClient(name = "nchuOJ-backend-user-service",path = "/api/user/inner")
public interface UserFeignClient {
    /**
     * 根据用户id获取用户信息
      * @param userld
     * @return
     */
    @GetMapping("/get/{id}")
    User getByld(@RequestParam("userld") long userld);


    /**
     * 获取用户信息
     * @param userIdSet
     * @return
     */
    @GetMapping("/get/ids")
    List<User> listBylds(@RequestParam("userIdSet")Collection<Long> userIdSet);



    default User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user){
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }


    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}
