package org.lorenz.fw.persistence.datadict;

import org.w3c.dom.Document;

public class CodeGenerator
{

	public static void main(String[] args)
	{
		String schemaPath = "src/main/resources/employee-schema.xml";

		SchemaParser parser = new SchemaParser();
		Document doc = parser.parseSchema(schemaPath);

		if (doc == null)
		{
			System.err.println("Failed to parse XML schema.");
			return;
		}

		doc.getDocumentElement().normalize();

		EntityGenerator generator = new EntityGenerator();
		generator.generateEntityClass(doc);
	}
}
