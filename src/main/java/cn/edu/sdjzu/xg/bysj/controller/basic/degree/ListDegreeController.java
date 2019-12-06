package cn.edu.sdjzu.xg.bysj.controller.basic.degree;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/degreeController/list.ctl")
public class ListDegreeController extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        //获得所有学位
        Collection<Degree> degrees = null;
        try {
            degrees = DegreeService.getInstance().findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String degrees_str = JSON.toJSONString(degrees);
        //向客户端响应数据
        response.getWriter().println(degrees_str);
    }
}
