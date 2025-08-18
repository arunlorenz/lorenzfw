package org.lorenz.fw.persistence.datadict;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class SchemaParser
{

	public Document parseSchema(String filePath)
	{
		try
		{
			File file = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.parse(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
