package org.springframework.samples.petclinic.metrics;

import io.micrometer.prometheus.PrometheusMeterRegistry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PrometheusMetricsServlet extends HttpServlet {
    private final PrometheusMeterRegistry prometheusMeterRegistry;

    public PrometheusMetricsServlet(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain; version=0.0.4");
        resp.getWriter().write(prometheusMeterRegistry.scrape());
    }
}
