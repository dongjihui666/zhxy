package com.dong.myzhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dong.myzhxy.pojo.Admin;
import com.dong.myzhxy.pojo.LoginForm;
import com.dong.myzhxy.pojo.Student;
import com.dong.myzhxy.pojo.Teacher;
import com.dong.myzhxy.service.AdminService;
import com.dong.myzhxy.service.StudentService;
import com.dong.myzhxy.service.TeacherService;
import com.dong.myzhxy.util.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 非针对表格增删改查
 */
@RestController //不是@Controller
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    /**
     * 修改密码的处理器
     */
    @ApiOperation("更新用户密码的方法")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @RequestHeader("token") String token,
            @PathVariable("oldPwd") String oldPwd,
            @PathVariable("newPwd") String newPwd
    ){
            // 判断token
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            // token过期
            return Result.fail().message("token失效,请重新登录后修改密码");
        }
        // 获取用户ID和用户的类型(根据token解析)
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        // 旧密码和新密码加密
        oldPwd  = MD5.encrypt(oldPwd);
        newPwd  = MD5.encrypt(newPwd);

        // 判断用户类型
        switch (userType){
            case 1:
                QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("id",userId.intValue());
                adminQueryWrapper.eq("password",oldPwd);
                Admin admin  =  adminService.getOne(adminQueryWrapper);
                if (admin != null){
                    //修改密码
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("原密码有误!");
                }
                break;

            case 2:
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("id",userId.intValue());
                studentQueryWrapper.eq("password",oldPwd);
                Student student  =  studentService.getOne(studentQueryWrapper);
                if (student != null){
                    //修改密码
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("原密码有误!");
                }
                break;

            case 3:
                QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
                teacherQueryWrapper.eq("id",userId.intValue());
                teacherQueryWrapper.eq("password",oldPwd);
                Teacher teacher  =  teacherService.getOne(teacherQueryWrapper);
                if (teacher != null){
                    //修改密码
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("原密码有误!");
                }
                break;
        }
        return Result.ok();
    }


    /**
     * 图片上传
     *
     */
    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    // 参数里面想要接收图片 用MultipartFile  @RequestPart
    public Result headerImgUpload(
            @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ){

        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        // 获取原始文件名称
        String originalFilename = multipartFile.getOriginalFilename();
        // 截取图片名称.后面的格式
        int i = originalFilename.lastIndexOf(".");
        //concat == +
        String newFileName = uuid.concat(originalFilename.substring(i)) ;

        // 保存文件 将文件发送到第三方/独立的图片服务器上,
        String portraitPath = "/Users/dongzhaihui/IdeaProjects/myzhxy/target/classes/com/public/upload" .concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 响应图片的路径
        String path = "upload/".concat(newFileName);
        return Result.ok(path);
    }

    /**
     * 接收Token
     * @return
     */
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        // 判断Token是否过期
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        // 从Token中解析出用户的ID和用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String,Object> map = new LinkedHashMap<>();

        // 判断用户的类型
        switch (userType){
            case 1:
                Admin admin =  adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student =  studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher =  teacherService.getteacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;

        }


        return Result.ok(map);
    }


    /**
     *  校验登录是否成功
     *
     */
    @PostMapping("/login")
    public Result Login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        // 验证码是否有效
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode){
            return Result.fail().message("验证码失效,请刷新后重试");
        }
        if(!sessionVerifiCode.equals(loginVerifiCode)){
            return Result.fail().message("验证码有误,请刷新后重试");
        }
        // 从session域中移除验证码
        session.removeAttribute("verifiCode");

        //

        // 分用户类型进行校验

        //准备一个map用户存放响应多大的数据
        Map<String,Object> map = new LinkedHashMap<String,Object>();
        switch (loginForm.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        //用户的类型和用户的ID转换成一个秘闻,以token的名称向客户端返回
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token", token);
                    } else {
                        // 手动抛出异常
                        throw new RuntimeException("用户名或者是密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或者是密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或者是密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");
    }



    @GetMapping("getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本放入session域,为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        //将验证码图片响应给浏览器
        //ServletOutputStream outputStream = response.getOutputStream();
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
