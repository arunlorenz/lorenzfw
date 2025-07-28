package org.lorenz.fw.Security;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

import org.lorenz.fw.Security.APISecurityConfigLoader.ApiConfig;
import org.lorenz.fw.Security.APISecurityConfigLoader.ApiParam;

public class APISecurityFilter implements Filter
{

	private Map<String, Map<String, ApiConfig>> methodUriMap;

	@Override public void init(FilterConfig filterConfig) throws ServletException
	{
		try
		{
			String configDir = filterConfig.getServletContext().getRealPath("/WEB-INF/api-security");
			methodUriMap = APISecurityConfigLoader.loadAllConfigs(configDir);
		}
		catch (Exception e)
		{
			throw new ServletException("Failed to load API security configs", e);
		}
	}

	@Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;

		String method = httpReq.getMethod().toUpperCase();

		String fullURI = httpReq.getRequestURI();         // e.g., /onepass/api/contractor/list
		String contextPath = httpReq.getContextPath();     // e.g., /onepass
		String path = fullURI.substring(contextPath.length());  // -> /api/contractor/list

		Map<String, ApiConfig> apiMap = methodUriMap.get(method);
		if (apiMap == null || !apiMap.containsKey(path))
		{
			httpResp.sendError(HttpServletResponse.SC_FORBIDDEN, "API not allowed for method: " + method);
			return;
		}

		ApiConfig config = apiMap.get(path);

		for (ApiParam param : config.getParams())
		{
			String value = httpReq.getParameter(param.getName());

			if (!param.isValid(value))
			{
				httpResp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter: " + param.getName());
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@Override public void destroy()
	{
	}
}
