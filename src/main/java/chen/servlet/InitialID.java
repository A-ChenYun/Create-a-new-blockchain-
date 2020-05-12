package chen.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.UUID;

/**
 * @Author AChen
 * @Data: 2020/5/11 11:58 上午
 */

public class InitialID implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        servletContext.setAttribute("uuid", uuid);
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
