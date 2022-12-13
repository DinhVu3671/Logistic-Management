package com.vrp.demo.configuration.multitenancy;

import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component(value = "requestInterceptor")
public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    @Lazy
    private UserSessionService userSessionService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) throws Exception {
        System.out.println("In preHandle we are Intercepting the Request");
        System.out.println("____________________________________________");
        String requestURI = request.getRequestURI();
//        String tenantID = request.getHeader("X-TenantID");
        String sessionID = request.getHeader("sessionID");
        TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT);
        UserSessionModel userSessionModel = userSessionService.getBySessionId(sessionID);
        String tenantID = userSessionModel == null ? null : userSessionModel.getUser().getUserName();
        System.out.println("RequestURI::" + requestURI + " || Search for TenantID  :: " + tenantID);
        System.out.println("____________________________________________");
        if (isCommonURI(requestURI) || request.getMethod().equals("OPTIONS")) {
            TenantContext.setCurrentTenant(TenantContext.DEFAULT_TENANT);
            return true;
        }
        // remove OPTIONS method to pass Preflight request from browser
        if (tenantID == null && !request.getMethod().equals("OPTIONS")) {
            response.getWriter().write("Your session has expired. Please login for new session?");
            response.setStatus(401);
            return false;
        }
        else {
            TenantContext.setCurrentTenant(tenantID);
            response.setHeader("X-TenantID", tenantID);
        }
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        TenantContext.clear();
    }

    private static boolean isCommonURI(String requestURI) {
        List<String> commonURIs = Arrays.asList("/sign-in", "/sign-up", "/get-current-user",
                "/sign-up-by-gmail", "/login",
                "/sign-out","/routes/hello");
        for (String URI : commonURIs) {
            if (requestURI.compareTo(URI) == 0)
                return true;
        }
        return false;
    }

}
