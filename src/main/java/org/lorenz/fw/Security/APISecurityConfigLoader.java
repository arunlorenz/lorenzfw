package org.lorenz.fw.Security;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class APISecurityConfigLoader
{

	/**
	 * Loads all API security configurations from XML files for GET, POST, PUT, DELETE.
	 */
	public static Map<String, Map<String, ApiConfig>> loadAllConfigs(String configDir) throws Exception
	{
		Map<String, Map<String, ApiConfig>> methodMap = new HashMap<>();

		for (String method : List.of("GET", "POST", "PUT", "DELETE"))
		{
			File file = new File(configDir, method.toLowerCase() + ".xml");
			if (!file.exists())
			{
				// Optional: log and continue instead of throwing
				continue;
			}

			Map<String, ApiConfig> apiConfigs = parseApiXml(file);
			methodMap.put(method, apiConfigs);
		}

		return methodMap;
	}

	/**
	 * Parses an XML file and loads the API URI and allowed parameters.
	 */
	private static Map<String, ApiConfig> parseApiXml(File file) throws Exception
	{
		Map<String, ApiConfig> apiMap = new HashMap<>();

		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		NodeList apis = doc.getElementsByTagName("api");

		for (int i = 0; i < apis.getLength(); i++)
		{
			Element apiElement = (Element) apis.item(i);
			String uri = apiElement.getAttribute("uri").trim();

			if (uri.isEmpty())
			{
				throw new IllegalArgumentException("Missing 'uri' attribute in <api> element in file: " + file.getName());
			}

			// Normalize URI to start with "/api/"
			if (!uri.startsWith("/"))
			{
				uri = "/" + uri;
			}
			if (!uri.startsWith("/api/"))
			{
				uri = "/api" + uri;
			}

			List<ApiParam> params = new ArrayList<>();
			NodeList paramNodes = apiElement.getElementsByTagName("param");

			for (int j = 0; j < paramNodes.getLength(); j++)
			{
				Element paramElem = (Element) paramNodes.item(j);

				String name = paramElem.getAttribute("name");
				String type = paramElem.getAttribute("type");
				String regex = paramElem.getAttribute("regex");
				boolean optional = Boolean.parseBoolean(paramElem.getAttribute("optional"));

				if (name == null || name.isEmpty())
				{
					throw new IllegalArgumentException("Missing 'name' attribute for a <param> in URI: " + uri);
				}

				params.add(new ApiParam(name, type, regex, optional));
			}

			apiMap.put(uri, new ApiConfig(uri, params));
		}

		return apiMap;
	}

	/**
	 * Encapsulates a single API config (URI + allowed params).
	 */
	public static class ApiConfig
	{
		private final String uri;
		private final List<ApiParam> params;

		public ApiConfig(String uri, List<ApiParam> params)
		{
			this.uri = uri;
			this.params = params;
		}

		public String getUri()
		{
			return uri;
		}

		public List<ApiParam> getParams()
		{
			return params;
		}

		public Optional<ApiParam> getParamByName(String name)
		{
			return params.stream().filter(p -> p.getName().equals(name)).findFirst();
		}
	}

	/**
	 * Represents a single allowed query/body parameter for a URI.
	 */
	public static class ApiParam
	{
		private final String name;
		private final String type;
		private final String regex;
		private final boolean optional;

		public ApiParam(String name, String type, String regex, boolean optional)
		{
			this.name = name;
			this.type = type;
			this.regex = regex;
			this.optional = optional;
		}

		public String getName()
		{
			return name;
		}

		public boolean isOptional()
		{
			return optional;
		}

		public boolean isValid(String value)
		{
			if (value == null)
				return optional;

			// Validate type
			if ("int".equalsIgnoreCase(type))
			{
				try
				{
					Integer.parseInt(value);
				}
				catch (NumberFormatException e)
				{
					return false;
				}
			}
			else if ("double".equalsIgnoreCase(type))
			{
				try
				{
					Double.parseDouble(value);
				}
				catch (NumberFormatException e)
				{
					return false;
				}
			}

			// Validate regex
			if (regex != null && !regex.isEmpty())
			{
				return value.matches(regex);
			}

			return true;
		}
	}
}
