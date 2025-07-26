package org.lorenz.fw.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class APIHandler
{
	public Object preProcess(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		return null;
	}

	public Object doProcess(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		return null;
	}

	public Object postProcess(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		return null;
	}
}
