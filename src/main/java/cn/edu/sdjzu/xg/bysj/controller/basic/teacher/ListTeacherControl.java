package cn.edu.sdjzu.xg.bysj.controller.basic.teacher;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/teacher/list.ctl")
public class ListTeacherControl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Collection<Teacher> teachers = null;
        try {
            teachers = TeacherService.getInstance().findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        teachers.clear();
//        request.setAttribute("teachers", teachers);
//        request.getRequestDispatcher("/WEB-INF/pages/teacher/list_table.jsp")
//                .forward(request,response);

        String teachers_str= JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(teachers_str);
        response.getWriter().println(teachers_str);
    }
}
