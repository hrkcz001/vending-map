package dev.morozan1.server.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

//@Component
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // Log the request details
        System.out.println("Received request: " +
                httpServletRequest.getMethod() + " " +
                httpServletRequest.getRequestURI() + "?" +
                httpServletRequest.getQueryString());

        // Log the request body
        String requestBody = getRequestBody(httpServletRequest);
        System.out.println("Request body: " + requestBody);

        // Continue with the request
        chain.doFilter(request, response);
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        try (InputStream inputStream = request.getInputStream()) {
            return StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        }
    }
}