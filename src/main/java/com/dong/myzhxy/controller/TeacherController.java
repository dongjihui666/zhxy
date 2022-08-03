package com.dong.myzhxy.controller;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.myzhxy.pojo.Teacher;
import com.dong.myzhxy.service.TeacherService;
import com.dong.myzhxy.util.MD5;
import com.dong.myzhxy.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //不是@Controller
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    /**
     * 查询讲师信息带分页
     */
    @ApiOperation("分页获取教师信息,带搜索条件")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(@ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
                              @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
                              Teacher teacher){

        Page<Teacher> pageParam = new Page<>(pageNo, pageSize);
        IPage<Teacher> iPage =  teacherService.getTeachersByOpr(pageParam, teacher);

        return Result.ok(iPage);
    }

    /**
     * 新增和修改
     */

    @PostMapping("saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher){

        Integer id = teacher.getId();
        if (id == null || id == 0){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);

        return  Result.ok();
    }

    /**
     * 删除批量删除
     */
    @ApiOperation("删除单个或者多个教师信息")
    @DeleteMapping("deleteTeacher")
    public Result deleteTeacher(@RequestBody List<Integer> ids){

        teacherService.removeByIds(ids);

        return Result.ok();
    }


}
