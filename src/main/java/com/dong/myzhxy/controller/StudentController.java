package com.dong.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.myzhxy.pojo.Student;
import com.dong.myzhxy.service.StudentService;
import com.dong.myzhxy.util.MD5;
import com.dong.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生")
@RestController //不是@Controller
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 删除单个或者多个学生信息
     * @param ids
     * @return
     */
    @ApiOperation("删除单个或者多个学生信息")
    @DeleteMapping("delStudentById")
    public Result delStudentById(@ApiParam("要删除的学生编号的json数组") @RequestBody List<Integer> ids){

        studentService.removeByIds(ids);
        return  Result.ok();
    }

    /**
     * 添加学生和修改学生信息
     * @return
     */
    @ApiOperation("保存或者修改学生信息")
    @PostMapping("addOrUpdateStudent")
    public Result addOrUpdateStudent(@ApiParam("要保存或者修改") @RequestBody Student student){

         // 是否需要密码转换为密文  新增学生的时候把密码转换成密文
        Integer id = student.getId();
        if (null == id || 0 == id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);

        return Result.ok();
    }

    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询的条件") Student student){

        // 分页信息封装page对象
        Page<Student> pageParam = new Page<>(pageNo,pageSize);
        // 进行查询
        IPage<Student> studentPage =  studentService.getStudentByOpr(pageParam,student);
        // 封装Result返回
        return  Result.ok(studentPage);
    }

}
