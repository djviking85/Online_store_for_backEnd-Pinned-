package com.example.store.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {
    /**
     This method is called when a request is being processed by the servlet container.
     It adds the "Access-Control-Allow-Credentials" header to the response and
     passes the request and response objects to the next filter in the chain.
     @param httpServletRequest the request object for the current HTTP request
     @param httpServletResponse the response object for the current HTTP response
     @param filterChain the chain of filters that will process the request
     @throws ServletException if a servlet-specific error occurs
     @throws IOException if an I/O error occurs during the processing of the request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
