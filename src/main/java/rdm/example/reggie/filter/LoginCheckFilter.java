package rdm.example.reggie.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import rdm.example.reggie.common.BaseContext;
import rdm.example.reggie.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description检查用户是否完成了登录
 * @Author rdm
 * @data 2022/5/24 - 17:07
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter" , urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    //过滤方法
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取本次请求的uri
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);

        String urls[] = new String[]{
          //放不需要拦截的请求路径,直接放行
          "/employee/login" ,
          "/employee/logout",
          "/backend/**",
          "/front/**",
          "/common/**",
          "/user/sendMsg",
          "/user/login"
        };

        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.若不需要处理直接放行
        if (check) {
            log.info("本次请求不需要处理");
            filterChain.doFilter(request, response);
            return;
        }

        //4-1.判断员工管理端登录状态如果已经登录，放行
        Long employee = (Long) request.getSession().getAttribute("employee");
        //把currentid赋值给Basecontext方便自动注入信息调用id
        BaseContext.setCurrentId(employee);

        if (employee != null) {
            log.info("用户" + employee + "已登录");
            filterChain.doFilter(request, response);
            return;
        }

        //4-2.判断用户移动端登录状态如果已经登录，放行
        Long user = (Long) request.getSession().getAttribute("user");
        //把currentid赋值给Basecontext方便自动注入信息调用id
        BaseContext.setCurrentId(user);

        if (user != null) {
            log.info("用户" + user + "已登录");
            filterChain.doFilter(request, response);
            return;
        }


        log.info("用户未登录");
        //5.未登录返回登录，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String urls[] , String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
