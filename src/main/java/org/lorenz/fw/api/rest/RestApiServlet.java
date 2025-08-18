package org.lorenz.fw.api.rest;

import org.lorenz.fw.utils.MapToJSONConvertorUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestApiServlet extends HttpServlet
{
	private static final Logger logger = Logger.getLogger(RestApiServlet.class.getName());
	private Map<String, ApiConfigLoader.ApiRoute> routeMap;

	@Override public void init() throws ServletException
	{
		try
		{
			logger.log(Level.INFO, "RestApiServlet initialized");
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

	@Override protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		handleRequest(req, resp);
	}

	@Override protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		handleRequest(req, resp);
	}

	private Object handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String fullURI = req.getRequestURI();         // e.g., /onepass/api/contractor/list
		String contextPath = req.getContextPath();     // e.g., /onepass
		String path = fullURI.substring(contextPath.length());  // -> /api/contractor/list

		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json"); // Set JSON response type
		System.out.println("➡️ Incoming API request: " + path);

		ApiConfigLoader.ApiRoute route = routeMap.get(path);
		if (route == null)
		{
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.println("{\"error\": \"API route not found: " + path + "\"}");
			return null;
		}

		try
		{
			Class<?> clazz = Class.forName(route.className);
			Object instance = clazz.getDeclaredConstructor().newInstance();
			Method method = clazz.getMethod(route.methodName, HttpServletRequest.class, HttpServletResponse.class);
			Object response = method.invoke(instance, req, resp);

			// 🔁 Auto-convert common types to JSON
			String jsonResponse = convertToJson(response);
			out.print(jsonResponse);
			return response;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.println("{\"error\": \"" + e.getClass().getSimpleName() + ": " + e.getMessage() + "\"}");
		}

		return null;
	}

	private String convertToJson(Object response)
	{
		if (response == null)
		{
			return "{}";
		}

		if (response instanceof Map)
		{
			return MapToJSONConvertorUtil.convertMapToJson((Map<String, Object>) response);
		}

		if (response instanceof List)
		{
			return MapToJSONConvertorUtil.convertListOfMapsToJson((List<Map<String, Object>>) response);
		}

		return "\"" + response.toString() + "\"";
	}
}
