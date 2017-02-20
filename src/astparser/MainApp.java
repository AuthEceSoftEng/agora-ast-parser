package astparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parser.ASTParser;

/**
 * Contains the main class of the application.
 * 
 * @author themis
 */
public class MainApp {

	/**
	 * Prints the help message of the command line interface.
	 */
	private static void printHelpMessage() {
		System.out.println("ASTParser: Abstract Syntax Tree Parser for Java Source Code\n");
		System.out.println("Run as:\n java -jar ASTParser.jar -project=\"path/to/project\"");
		System.out.println("Or as:\n java -jar ASTParser.jar -file=\"path/to/file\"");
	}

	/**
	 * Executes the application.
	 * 
	 * @param args arguments for executing in command line mode.
	 */
	public static void main(String args[]) {
		if (args.length > 0) {
			List<String> arguments = new ArrayList<String>();
			for (String arg : args) {
				String narg = arg.trim();
				if (narg.contains("=")) {
					for (String n : narg.split("=")) {
						arguments.add(n);
					}
				} else
					arguments.add(arg.trim());
			}
			arguments.removeAll(Arrays.asList("", null));
			boolean project = arguments.get(0).equals("-project");
			boolean file = arguments.get(0).equals("-file");
			if (project ^ file) {
				String result = "";
				if (project)
					result = ASTParser.parseProject(arguments.get(1));
				else if (file)
					result = ASTParser.parseFile(arguments.get(1));
				System.out.println(result);
			} else {
				printHelpMessage();
			}
		} else {
			printHelpMessage();
		}
	}

}
