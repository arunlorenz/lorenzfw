package org.lorenz.fw.Security;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CORSFilter implements Filter
{
	private static final Logger logger = Logger.getLogger(CORSFilter.class.getName());

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletResponse res = (HttpServletResponse) response;

		res.setHeader("Access-Control-Allow-Origin", "*"); // Allow all origins (OK for dev only)
		res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig)
	{
		logger.log(Level.INFO, "CORSFilter initialized");
		System.out.println("✅ CORSFilter initialized");
	}

	public void destroy()
	{
	}
}
