package com.jueye.nchuojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户视图（脱敏）
 *
 
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;


    private static final long serialVersionUID = 1L;
}