package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter 0", urlPatterns = "/*")
public class Filter00_Encoding implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 0 - encoding begin");

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response= (HttpServletResponse)resp;
        String path = request.getRequestURI();
//        if (!path.contains("/login")){
            response.setContentType("text/html;charset=UTF-8");
            if (request.getMethod() == "POST" || request.getMethod() == "GET")
                response.setContentType("text/html;charset=UTF-8");
//        }
        chain.doFilter(req, resp);
        System.out.println("Filters 0 - encoding ends");
    }
}
