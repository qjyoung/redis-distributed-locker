package com.siyueren.demo.redisdistributedlocker.controller;

import com.siyueren.demo.redisdistributedlocker.util.ExcelExport;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 乔健勇
 * @date 19:57 2021/1/21
 * @email qjyoung@163.com
 */
@RestController
public class ExportDemoController {
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        // handlers
        ExcelExport.ValueHandlerList<User> handlers = new ExcelExport.ValueHandlerList<>();
        handlers.append("姓名", User::getName);
        handlers.append("性别", User::getSex);
        handlers.append("生日", 20, user -> formatToString(user.getBirthday() * 1000));
        handlers.append("作品", 50, User::getWorks);
        // 构造excel导出对象
        ExcelExport excelExport = new ExcelExport(response, "idols.xls");
        // 中国演员
        excelExport.writeToFile("中国", getChinese(), handlers);
        // 外国演员
        excelExport.writeToFile("外国", getForeigners(), handlers);
        // 导出
        excelExport.export();
    }
    
    public static String formatToString(long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(time));
    }
    
    @Data
    @AllArgsConstructor
    public static class User {
        String name;
        String sex;
        long birthday;
        String works;
    }
    
    public List<User> getChinese() {
        List<User> users = new ArrayList<>();
        users.add(new User("成龙", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        users.add(new User("成龙", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        users.add(new User("成龙", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        users.add(new User("成龙", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        users.add(new User("成龙", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        return users;
    }
    
    public List<User> getForeigners() {
        List<User> users = new ArrayList<>();
        users.add(new User("JackChan", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        users.add(new User("JackChan", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        users.add(new User("JackChan", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        users.add(new User("JackChan", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        users.add(new User("JackChan", "男", -496583483L, "《龙兄虎弟》《我是谁》《尖峰时刻》《飞鹰计划》《醉拳》"));
        return users;
    }
}
