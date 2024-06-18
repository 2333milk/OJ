package com.jueye.nchuojbackenduserservice.controller.inner;

import com.jueye.nchuojbackendmodel.model.entity.User;
import com.jueye.nchuojbackendserviceclient.service.UserFeignClient;
import com.jueye.nchuojbackenduserservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;


/**
 * 用户接口
 *
 
 */
@RestController("/inner")
@Slf4j
public class UserInnerController implements UserFeignClient {
    @Resource
    private UserService userService;

    @Override
    @GetMapping("/get/{id}")
    public User getByld(long userld) {
        return this.userService.getById(userld);
    }

    @Override
    @GetMapping("/get/ids")
    public List<User> listBylds(Collection<Long> userIdSet) {
        return this.userService.listByIds(userIdSet);
    }
}
