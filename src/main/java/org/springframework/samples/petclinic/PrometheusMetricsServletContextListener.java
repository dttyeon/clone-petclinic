package org.springframework.samples.petclinic;

import io.micrometer.prometheus.PrometheusMeterRegistry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class PrometheusMetricsServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (rootContext == null) {
            throw new IllegalStateException("Root WebApplicationContext is not initialized yet.");
        }

        PrometheusMeterRegistry registry = rootContext.getBean(PrometheusMeterRegistry.class);

        PrometheusMetricsServlet servlet = new PrometheusMetricsServlet(registry);
        ServletRegistration.Dynamic reg = servletContext.addServlet("prometheusMetrics", servlet);
        reg.addMapping("/metrics");
        reg.setLoadOnStartup(1);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Do nothing
    }
}
