package org.lorenz.fw.lmi;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeTrackFilter implements Filter
{
	private static final Logger logger = Logger.getLogger(TimeTrackFilter.class.getName());
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	@Override public void init(FilterConfig filterConfig) throws ServletException
	{
		logger.log(Level.INFO, "TimeTrackerFilter initialized");
		System.out.println("✅ TimeTrackerFilter initialized");
	}

	@Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String requestURI = req.getRequestURI();

		long startTime = System.currentTimeMillis();
		logger.log(Level.INFO, "Request URI: " + requestURI);
		logger.log(Level.INFO, "Request Entered TimeTrackerFilter at: " + sdf.format(new Date(startTime)));

		chain.doFilter(request, response);

		long endTime = System.currentTimeMillis();
		logger.log(Level.INFO, "Request Exited TimeTrackerFilter at: " + sdf.format(new Date(endTime)));

		long totalTime = endTime - startTime;
		logger.log(Level.INFO, "Total time taken for the request: " + totalTime + " ms");
	}

	@Override public void destroy()
	{

	}
}
