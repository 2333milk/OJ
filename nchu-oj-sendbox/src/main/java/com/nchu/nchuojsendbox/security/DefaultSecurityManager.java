package com.nchu.nchuojsendbox.security;

import java.security.Permission;

public class DefaultSecurityManager extends SecurityManager{

    //检查所有的权限


    @Override
    public void checkPermission(Permission perm) {
        System.out.println("默认不限制");
        super.checkPermission(perm);
    }

}
