package test;

import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import com.alibaba.fastjson.JSON;

import java.sql.SQLException;

public class AddDepartmentPostmanTest {
    public static void main(String[] args){
        String list = "{\"description\":\"信息管理\",\"id\":1,\"no\":\"0201\",\"remarks\":\"不当网管\",\"school\":{\"description\":\"宇宙大学\",\"id\":26,\"no\":\"04\",\"remarks\":\"筑基建业\"}}";
        Department departmentToAdd = JSON.parseObject(list, Department.class);
        try {
            DepartmentService.getInstance().update(departmentToAdd);
            System.out.println("成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
