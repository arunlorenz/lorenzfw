package org.lorenz.fw.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersistenceCore
{
	private static final Logger logger = Logger.getLogger(PersistenceCore.class.getName());

	private static final String URL = "jdbc:mysql://localhost:3306/lorenzdb";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	static
	{
		try
		{
			logger.log(Level.INFO, "Loading MySQL JDBC driver...");
			System.out.println("✅ Loading MySQL JDBC driver...");
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage());
			throw new RuntimeException("Failed to load MySQL JDBC driver", e);
		}
	}


	private static Connection getConnection() throws Exception
	{
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	private static PreparedStatement getPreparedStatement(String query) throws Exception
	{
		return getConnection().prepareStatement(query);
	}

	protected static Object getData(String query) throws Exception
	{
		PreparedStatement statement = getPreparedStatement(query);
		return statement.executeQuery();
	}

	protected static Object getDataObject(String className, int id) throws Exception
	{
		SessionFactory factory = new Configuration().configure().buildSessionFactory();
		Session session = factory.openSession();
		return session.get(className, id);
	}

	protected static void addData(Object object) throws Exception
	{
		SessionFactory factory = new Configuration().configure().buildSessionFactory();
		Session session = factory.openSession();

		try
		{
			session.beginTransaction();
			session.persist(object);
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			session.getTransaction().rollback();
		}
		finally
		{
			session.close();
			factory.close();
		}
	}

	protected static void update(Object object) throws Exception
	{
		SessionFactory factory = new Configuration().configure().buildSessionFactory();
		Session session = factory.openSession();

		try
		{
			session.beginTransaction();
			session.update(object);
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			session.getTransaction().rollback();
		}
		finally
		{
			session.close();
			factory.close();
		}
	}

	protected static void delete(Object object) throws Exception
	{
		SessionFactory factory = new Configuration().configure().buildSessionFactory();
		Session session = factory.openSession();

		try
		{
			session.beginTransaction();
			session.delete(object);
			session.getTransaction().commit();
		}
		catch (Exception e)
		{
			session.getTransaction().rollback();
			throw e;
		}
		finally
		{
			session.close();
			factory.close();
		}
	}
}
