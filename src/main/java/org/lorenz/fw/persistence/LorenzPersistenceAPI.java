package org.lorenz.fw.persistence;

import java.sql.ResultSet;

public class LorenzPersistenceAPI
{
	public static ResultSet get(String query) throws Exception
	{
		return (ResultSet) PersistenceCore.getData(query);
	}

	public static Object getObject(String className, int id) throws Exception
	{
		return PersistenceCore.getDataObject(className, id);
	}

	public static void add(Object object) throws Exception
	{
		PersistenceCore.addData(object);
	}

	public static void update(Object object) throws Exception
	{
		PersistenceCore.update(object);
	}

	public static void delete(Object object) throws Exception
	{
		PersistenceCore.delete(object);
	}
}
