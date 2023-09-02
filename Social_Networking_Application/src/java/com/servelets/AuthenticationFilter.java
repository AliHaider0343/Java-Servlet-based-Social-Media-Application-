/**
 * This filter is responsible for validating pages that are authorized and only allows access to users with an account.
 *
 * @author Jose Omar
 * @version 1.0
 * @since 2023-05-14
 */
package com.servelets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.*;

public class AuthenticationFilter implements Filter {

    /**
     * Initializes the filter.
     *
     * @param config the filter configuration object
     * @throws ServletException if an error occurs during initialization
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * Performs the filtering of requests.
     *
     * @param request the servlet request
     * @param response the servlet response
     * @param chain the filter chain
     * @throws IOException if an I/O error occurs during the filtering process
     * @throws ServletException if an error occurs during the filtering process
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        PrintWriter out = response.getWriter();

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (isLoggedIn) {
            chain.doFilter(request, response);
        } else {
            out.println("<script>alert('Please Login First and then Access Authorized Pages .')</script>");
            request.getRequestDispatcher("login.jsp").include(request, response);
        }
    }

    /**
     * Cleans up resources used by the filter.
     */
    @Override
    public void destroy() {
    }
}
