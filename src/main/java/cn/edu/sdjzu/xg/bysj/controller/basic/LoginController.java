package cn.edu.sdjzu.xg.bysj.controller.basic;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/login.ctl")
public class LoginController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //创建JSON对象message,向前端响应信息
        JSONObject message = new JSONObject();
        try {
            User loggedUser = UserService.getInstance().login(username,password);
            if (loggedUser != null){
                message.put("message","登陆成功!!");
                HttpSession session = request.getSession();
                //十分钟内没有操作,则session失效
                session.setMaxInactiveInterval(600);
                session.setAttribute("currentUser", loggedUser);
            }else
                message.put("message", "用户名或密码错误");
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message", "其他异常");
            e.printStackTrace();
        }
        response.getWriter().println(message);
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

    }
//    //响应一个专业对象
//    private void responseDepartment(int id, HttpServletResponse response)
//            throws IOException, SQLException {
//
//    }
//    //响应一个学位对象
//    private void responseUser(int id, HttpServletResponse response)
//            throws IOException, SQLException {
//        //根据id查找学位
//        User user = UserService.getInstance().find(id);
//        String user_json = JSON.toJSONString(user);
//
//        //响应degree_json到前端
//        response.getWriter().println(user_json);
//    }
//    //响应所有学位对象
//    private void responseUsers(HttpServletResponse response)
//            throws IOException, SQLException {
//        //获得所有学位
//        Collection<User> users = UserService.getInstance().findAll();
//        System.out.println(users.size());
//        String users_json = JSON.toJSONString(users, SerializerFeature.DisableCircularReferenceDetect);
//
//        //响应degrees_json到前端
//        response.getWriter().println(users_json);
//        System.out.println("json:" + users_json);
//    }


}
