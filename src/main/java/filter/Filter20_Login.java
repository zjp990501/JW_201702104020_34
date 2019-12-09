package filter;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter20_Login",  urlPatterns = "/*")
public class Filter20_Login implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 20 - login begin");

        HttpServletResponse response = (HttpServletResponse)resp;
        HttpServletRequest request = (HttpServletRequest)req;

        String path = request.getRequestURI();
        JSONObject message = new JSONObject();

        HttpSession session = request.getSession(false);

        if (!path.contains("/log") &&
                (session == null ||
                        (session != null && session.getAttribute("currentUser") == null))){
                message.put("message", "请登录或者重新登录");
                response.getWriter().println(message);
        }else
            chain.doFilter(req, resp);
        System.out.println("Filter 20 - login end");
    }
}
