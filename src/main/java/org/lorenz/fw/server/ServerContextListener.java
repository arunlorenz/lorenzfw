package org.lorenz.fw.server;

import org.lorenz.fw.lmi.TerminalOutput;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerContextListener implements ServletContextListener
{
	private static final Logger logger = Logger.getLogger(ServletContextListener.class.getName());

	@Override public void contextInitialized(ServletContextEvent sce)
	{
		logger.log(Level.INFO, "ServerContextListener Initialization has been called at " + System.currentTimeMillis());
		TerminalOutput.println("ServerContextListener has been invoked");
	}

	@Override public void contextDestroyed(ServletContextEvent sce)
	{
		logger.log(Level.INFO, "ServerContextListener Deinitialization has been called at " + System.currentTimeMillis());
	}
}
