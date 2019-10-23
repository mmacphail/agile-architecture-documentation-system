package ${groupId}.architecture;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.structurizr.Workspace;
import com.structurizr.io.plantuml.PlantUMLWriter;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

public class Architecture {
	Logger logger = Logger.getLogger(Architecture.class.getName());

	@Parameter(names = "destination", converter = FileConverter.class)
	File destination;

	public static void main(String[] args) throws IOException {
		Architecture main = new Architecture();
		JCommander.newBuilder().addObject(main).build().parse(args);
		main.run();
	}

	public void run() throws IOException {
		Workspace workspace = describeArchitecture();

		StringWriter stringWriter = new StringWriter();
		PlantUMLWriter plantUMLWriter = new PlantUMLWriter();
		plantUMLWriter.addSkinParam("rectangleFontColor", "#ffffff");
		plantUMLWriter.addSkinParam("rectangleStereotypeFontColor", "#ffffff");
		plantUMLWriter.write(workspace, stringWriter);
		logger.info(String.format("We should write output to %s", destination.getAbsolutePath()));

		destination.getParentFile().mkdirs();
		Files.write(destination.toPath(), stringWriter.toString().getBytes());
	}

	/**
	 * Creates the workspace object and add in it both the architecture components
	 * AND the views used to display it
	 * 
	 * @return
	 */
	private static Workspace describeArchitecture() {
		Workspace workspace = new Workspace("Getting Started", "This is a model of my software system.");
		Model model = workspace.getModel();

		Person user = model.addPerson("User", "A user of my software system.");
		SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
		user.uses(softwareSystem, "Uses");

		ViewSet views = workspace.getViews();
		SystemContextView contextView = views.createSystemContextView(softwareSystem, "SystemContext",
				"An example of a System Context diagram.");
		contextView.addAllSoftwareSystems();
		contextView.addAllPeople();

//		Styles styles = views.getConfiguration().getStyles();
//		styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
//		styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
		return workspace;
	}

}