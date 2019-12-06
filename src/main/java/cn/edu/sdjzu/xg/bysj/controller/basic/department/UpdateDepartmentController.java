package cn.edu.sdjzu.xg.bysj.controller.basic.department;

import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/department/updateDepartment.ctl")
public class UpdateDepartmentController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int theID = Integer.parseInt(request.getParameter("id"));
            Department department = DepartmentService.getInstance().find(theID);
            String schools_str = JSON.toJSONString(department);
            response.getWriter().println(schools_str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //yzh
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int school_id = Integer.parseInt(request.getParameter("the_schoolID"));
            //本身是字符串,直接用
            Department departmentToUpdate = JSON.parseObject(request.getParameter("the_json"),Department.class);
            //单独更改school
            departmentToUpdate.setSchool(SchoolService.getInstance().find(school_id));
            DepartmentService.getInstance().update(departmentToUpdate);//更新
            response.getWriter().println(JSON.toJSONString(departmentToUpdate));
        }catch (NullPointerException e){
            //如果根据id的值找不到School对象，则会出现空指针异常
            response.getWriter().println("Invalid change! Please re-enter...     --by Servlet");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
