package cn.edu.sdjzu.xg.bysj.controller.basic.security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/showCookie")
public class CookieServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //得到所有cookie信息
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            response.getWriter().println("No cookies available");
        else
            for (Cookie cookie: cookies)
                response.getWriter().println(cookie.getName() + "=" + cookie.getValue());
    }
}
