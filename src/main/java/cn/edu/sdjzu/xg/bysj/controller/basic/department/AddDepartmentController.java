package cn.edu.sdjzu.xg.bysj.controller.basic.department;

import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/department/addDepartment.ctl")
public class AddDepartmentController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        String department_json = JSONUtil.getJSON(request);

        Department departmentToAdd = JSON.parseObject(department_json,Department.class);
        departmentToAdd.setId(4 + (int)(1000*Math.random()));

        try {
            DepartmentService.getInstance().add(departmentToAdd);
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        //创建JSON对象
//        JSONObject resp = new JSONObject();
//        //加入数据信息
//        resp.put("MSG", "OK");
//        //响应
//        response.getWriter().println(resp);
    }
}
