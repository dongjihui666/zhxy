package com.dong.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.myzhxy.pojo.Grade;
import com.dong.myzhxy.service.GradeService;
import com.dong.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:9001/sms/gradeController/getGrades/1/3 400
@Api(tags = "年级控制器")
@RestController //不是@Controller
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    /**
     * 查询所有年级
     */
    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades(){

        List<Grade> list = gradeService.list();

        return Result.ok(list);
    }


    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除的所有的grade的ID的json集合")
            @RequestBody List<Integer> ids){

        gradeService.removeByIds(ids);

        return Result.ok();
    }

    /**
     * 更新和修改
     * @param grade
     * @return
     */
    @ApiOperation("新增或修改Grade,有ID属性是修改,没有则是增加")
    @PostMapping("saveOrUpdateGrade")
    public Result savePrUpdateGrade(
            @ApiParam("JSON的Grade对象")
            @RequestBody Grade grade){

        // 接收参数
        // 调用服务层方法完成增加或者修改
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }


    /**
     * 分页带条件查询
     * @param pageNo
     * @param pageSize
     * @param gradeName
     * @return
     */
    @ApiOperation("根据年级名称模糊查询,带分页")
    //@RequestParam 获取请求的参数
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码数")
           @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小")
           @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配的名称")
           String gradeName
    ){
        // 分页  带条件查询
        Page<Grade> page = new Page<>(pageNo,pageSize);
        // 通过服务层进行查询
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page,gradeName);

        // 封装Result对象进行返回
        return  Result.ok(pageRs);
    }


}
