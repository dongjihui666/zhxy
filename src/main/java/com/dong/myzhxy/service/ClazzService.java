package com.dong.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dong.myzhxy.pojo.Clazz;

public interface ClazzService extends IService<Clazz> {
    IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz);
}
