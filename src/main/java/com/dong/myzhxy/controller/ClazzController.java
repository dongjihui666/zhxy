package com.dong.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.myzhxy.pojo.Clazz;
import com.dong.myzhxy.service.ClazzService;
import com.dong.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级管理器")
@RestController //不是@Controller
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 查询全部的班级信息
     * @param
     * @return
     */
    @ApiOperation("查询所有的班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){

        List<Clazz> list = clazzService.list();

        return Result.ok(list);
    }

    @ApiOperation("新增或修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@ApiParam("JSON的班级信息") @RequestBody Clazz clazz){

        clazzService.saveOrUpdate(clazz);

        return Result.ok();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @ApiOperation("批量删除班级信息")
    @DeleteMapping("deleteClazz")
    public Result deleteClazz(@ApiParam("要删除的所有的grade的ID的json集合")
                                  @RequestBody List<Integer> ids){

        clazzService.removeByIds(ids);
        return Result.ok();

    }


    @ApiOperation("分页带条件查询.查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页数大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件") Clazz clazz){
        Page<Clazz> page = new Page<Clazz>(pageNo,pageSize);
        IPage<Clazz> iPage = clazzService.getClazzByOpr(page,clazz);

        return Result.ok(iPage);

    }
}
