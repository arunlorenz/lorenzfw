package org.lorenz.fw.api.rest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

public class RestApiServlet extends HttpServlet
{
	private Map<String, ApiConfigLoader.ApiRoute> routeMap;

	@Override public void init() throws ServletException
	{
		try
		{
			System.out.println("✅ RestApiServlet initialized");
			routeMap = ApiConfigLoader.loadRoutes(getServletContext());
		}
		catch (Exception e)
		{
			throw new ServletException("Failed to load API routes", e);
		}
	}

	@Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		handleRequest(req, resp);
	}

	@Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		handleRequest(req, resp);
	}

	private void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String fullURI = req.getRequestURI();         // e.g., /onepass/api/contractor/list
		String contextPath = req.getContextPath();     // e.g., /onepass
		String path = fullURI.substring(contextPath.length());  // -> /api/contractor/list

		PrintWriter out = resp.getWriter();
		resp.setContentType("text/plain");
		System.out.println("➡️ Incoming API request: " + path);

		ApiConfigLoader.ApiRoute route = routeMap.get(path);
		if (route == null)
		{
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.println("API route not found: " + path);
			return;
		}

		try
		{
			Class<?> clazz = Class.forName(route.className);
			Object instance = clazz.getDeclaredConstructor().newInstance();
			Method method = clazz.getMethod(route.methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(instance, req, resp);
		}
		catch (Exception e)
		{
			e.printStackTrace(out);
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
