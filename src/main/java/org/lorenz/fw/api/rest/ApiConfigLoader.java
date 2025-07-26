package org.lorenz.fw.api.rest;

import org.w3c.dom.*;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ApiConfigLoader
{

	public static class ApiRoute
	{
		public String className;
		public String methodName;
	}

	public static Map<String, ApiRoute> loadRoutes(ServletContext context) throws Exception
	{
		Map<String, ApiRoute> routeMap = new HashMap<>();

		try (InputStream stream = context.getResourceAsStream("/WEB-INF/api-config.xml"))
		{
			if (stream == null)
			{
				throw new RuntimeException("api-config.xml not found in /WEB-INF/");
			}

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
			NodeList apis = doc.getElementsByTagName("api");

			for (int i = 0; i < apis.getLength(); i++)
			{
				Element api = (Element) apis.item(i);
				String path = api.getElementsByTagName("path").item(0).getTextContent();
				String className = api.getElementsByTagName("class").item(0).getTextContent();
				String methodName = api.getElementsByTagName("method").item(0).getTextContent();

				ApiRoute route = new ApiRoute();
				route.className = className;
				route.methodName = methodName;

				routeMap.put(path, route);
			}
		}

		return routeMap;
	}
}
