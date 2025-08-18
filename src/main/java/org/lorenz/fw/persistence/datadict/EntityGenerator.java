package org.lorenz.fw.persistence.datadict;

import org.w3c.dom.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EntityGenerator
{

	public void generateEntityClass(Document doc)
	{
		Element entityElement = (Element) doc.getElementsByTagName("entity").item(0);
		String className = entityElement.getAttribute("name");

		StringBuilder sb = new StringBuilder();
		sb.append("package org.lorenz.generated;\n\n");
		sb.append("import jakarta.persistence.*;\n\n");

		sb.append("@Entity\n");
		sb.append("@Table(name = \"").append(className.toLowerCase()).append("\")\n");
		sb.append("public class ").append(className).append(" {\n\n");

		NodeList fields = entityElement.getElementsByTagName("field");

		for (int i = 0; i < fields.getLength(); i++)
		{
			Element field = (Element) fields.item(i);
			String name = field.getAttribute("name");
			String type = field.getAttribute("type");

			sb.append("    @Column(name = \"").append(name).append("\")\n");

			if (i == 0)
			{
				// Assume first field is the ID
				sb.append("    @Id\n");
				sb.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
			}

			sb.append("    private ").append(type).append(" ").append(name).append(";\n\n");
		}

		// Generate getters and setters
		for (int i = 0; i < fields.getLength(); i++)
		{
			Element field = (Element) fields.item(i);
			String name = field.getAttribute("name");
			String type = field.getAttribute("type");
			String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);

			// Getter
			sb.append("    public ").append(type).append(" get").append(capitalized).append("() {\n").append("        return ").append(name).append(";\n").append("    }\n\n");

			// Setter
			sb.append("    public void set").append(capitalized).append("(").append(type).append(" ").append(name).append(") {\n").append("        this.").append(name).append(" = ").append(name).append(";\n").append("    }\n\n");
		}

		sb.append("}");

		// Write to file
		try
		{
			Path outputPath = Paths.get("src/main/java/org/lorenz/generated");
			Files.createDirectories(outputPath);

			FileWriter writer = new FileWriter(outputPath.resolve(className + ".java").toFile());
			writer.write(sb.toString());
			writer.close();

			System.out.println("Generated class: " + className + ".java");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
