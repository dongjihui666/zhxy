package com.dong.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dong.myzhxy.mapper.ClazzMapper;
import com.dong.myzhxy.pojo.Clazz;
import com.dong.myzhxy.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz) {
        QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<>();
        String gradeName = clazz.getGradeName();
        if (!StringUtils.isEmpty(gradeName)){
            clazzQueryWrapper.like("grade_name",gradeName);
        }
        String name = clazz.getName();
        if(!StringUtils.isEmpty(name)){
            clazzQueryWrapper.like("name",name);
        }
        clazzQueryWrapper.orderByDesc("id");
        Page<Clazz> page1 = baseMapper.selectPage(page, clazzQueryWrapper);

        return page1;
    }
}
