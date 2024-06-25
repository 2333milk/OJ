package com.jueye.nchuojbackendsendboxservice.security;

import java.security.Permission;

public class DenySecurityManager extends SecurityManager{

    //检查所有的权限


    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("权限异常");
    }

}
