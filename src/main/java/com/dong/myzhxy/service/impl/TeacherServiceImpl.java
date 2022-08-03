package com.dong.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dong.myzhxy.mapper.TeacherMapper;
import com.dong.myzhxy.pojo.LoginForm;
import com.dong.myzhxy.pojo.Teacher;
import com.dong.myzhxy.service.TeacherService;
import com.dong.myzhxy.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("name",loginForm.getUsername());
        teacherQueryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Teacher teacher = baseMapper.selectOne(teacherQueryWrapper);
        return teacher;
    }

    @Override
    public Teacher getteacherById(Long userId) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("id",userId);
        Teacher teacher = baseMapper.selectOne(teacherQueryWrapper);
        return teacher;
    }

    @Override
    public IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(teacher.getName())){
            queryWrapper.like("name",teacher.getName());
        }
        if (!StringUtils.isEmpty(teacher.getClazzName())){
            queryWrapper.eq("clazz_name",teacher.getClazzName());
        }
        queryWrapper.orderByDesc("id");

        Page<Teacher> teacherPage = baseMapper.selectPage(pageParam, queryWrapper);
        return teacherPage;
    }
}
