package com.dong.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dong.myzhxy.mapper.GradeMapper;
import com.dong.myzhxy.pojo.Grade;
import com.dong.myzhxy.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName) {
        QueryWrapper<Grade> objectQueryWrapper = new QueryWrapper();

        if (!StringUtils.isEmpty(gradeName)) {
            objectQueryWrapper.like("name",gradeName);
        }
        objectQueryWrapper.orderByDesc("id");

        Page<Grade> page1 = baseMapper.selectPage(page, objectQueryWrapper);
        return page1;
    }
}
