package com.dong.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.myzhxy.pojo.Admin;
import com.dong.myzhxy.pojo.LoginForm;

public interface AdminService extends IService<Admin> {

     Admin login(LoginForm loginForm);

     Admin getAdminById(Long userId);

    IPage<Admin> getAdminByOpr(Page<Admin> pageParam, String adminName);
}
