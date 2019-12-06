package cn.edu.sdjzu.xg.bysj.controller.basic.profTitle;

import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import cn.edu.sdjzu.xg.bysj.service.ProfTitleService;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/proTitle/listProTitle.ctl")
public class ListProfTitleController extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        //获得所有学位
        Collection<ProfTitle> profTitles = null;
        try {
            profTitles = ProfTitleService.getInstance().findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String degrees_str = JSON.toJSONString(profTitles);
        //向客户端响应数据
        response.getWriter().println(degrees_str);
    }
}
