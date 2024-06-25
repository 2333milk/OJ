package com.jueye.nchuojbackendsendboxservice.security;

import java.security.Permission;

public class MySecurityManager extends SecurityManager{

    //检查所有的权限
    @Override
    public void checkPermission(Permission perm) {
        super.checkPermission(perm);
    }
    //检查程序是否可执行文件
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("");
    }
    //检查程序是否允许读
    @Override
    public void checkRead(String file) {
        throw new SecurityException("");
    }
    //检查程序是否允许写
    @Override
    public void checkWrite(String file) {
        throw new SecurityException("");
    }
    //检查程序是否允许删除
    @Override
    public void checkDelete(String file) {
        throw new SecurityException("");
    }

    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("");
    }
}
