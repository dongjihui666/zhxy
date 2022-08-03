package com.dong.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dong.myzhxy.pojo.Admin;
import com.dong.myzhxy.service.AdminService;
import com.dong.myzhxy.util.MD5;
import com.dong.myzhxy.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //不是@Controller
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 分页带条件查询管理员
     */
    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(@ApiParam("页面数") @PathVariable("pageNo") Integer pageNo,
                              @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
                              @ApiParam("管理员名字") String adminName){
        Page<Admin> pageParam = new Page<>(pageNo, pageSize);
        IPage<Admin> iPage =  adminService.getAdminByOpr(pageParam,adminName);

        return Result.ok(iPage);
    }
    @ApiOperation("修改增加管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@ApiParam("JSON格式的Admin对象") @RequestBody Admin admin){

        // 新增的时候把密码转换成密文
        Integer id = admin.getId();
        if (id == null || 0 == id){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);

        return Result.ok();
    }
    @ApiOperation("删除单个或者多个用户管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@ApiParam("要删除管理员的多个ID") @RequestBody List<Integer> ids){

        adminService.removeByIds(ids);
        return Result.ok();
    }
}
