package parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTool;

import helpers.FileSystemHelpers;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import org.json.JSONObject;

/**
 * Contains all functions for extracting Abstract Syntax Trees (ASTs) from java files.
 * 
 * @author themis
 */
public class ASTParser {

	/**
	 * Parses all the files of a project and returns a unified AST.
	 * 
	 * @param projectPath the path of the project of which the files are parsed.
	 * @return an AST containing all the files of a folder in JSON format.
	 */
	public static String parseProject(String projectPath) {
		ArrayList<File> files = FileSystemHelpers.getJavaFilesOfFolderRecursively(projectPath);
		if (files.size() > 0){
			StringBuilder javaproject = new StringBuilder("{\n   "); 
			String filename;
			JSONObject fileAST;
			StringWriter w = new StringWriter();
			StringBuffer b = w.getBuffer();
			for (int i = 0; i < files.size() - 1; i++) {
				filename = files.get(i).getAbsolutePath();
				fileAST = parse(filename);
				fileAST.write(w, 3, 3);
				javaproject.append("\"" + filename + "\": {\n" + b.substring(2) + ",\n   ");
				b.setLength(0);
			}
			filename = files.get(files.size() - 1).getAbsolutePath();
			fileAST = parse(filename);
			fileAST.write(w, 3, 3);
			javaproject.append("\"" + filename + "\": {\n" + b.substring(2) + "\n}");
			return javaproject.toString();
		}
		else
			return "{}";
	}

	/**
	 * Parses a java file and returns its AST.
	 * 
	 * @param filename the filename of the java file to be parsed.
	 * @return a String containing the AST of the java file in JSON format.
	 */
	public static String parseFile(String filename) {
		return parse(filename).toString(3);
	}


	/**
	 * Parses the source code of a java file and returns its AST.
	 * 
	 * @param sourceCode the source code of a java file, given as a String.
	 * @return a String containing the AST of the java file in JSON format.
	 */
	public static String parseString(String sourceCode) {
		return parse("", sourceCode).toString(3);
	}

	/**
	 * Parses a java file and returns its AST.
	 * 
	 * @param filename the filename of the java file to be parsed.
	 * @return a String containing the AST of the java file in JSON format.
	 */
	private static JSONObject parse(String filename) {
		return parse(filename, null);
	}

	/**
	 * Parses the source code of a java entity, either file or source code, and
	 * returns its AST.
	 * 
	 * @param filename the filename of the java file to be parsed.
	 * @param sourceCode the source code of a java file, given as a String.
	 * @return a String containing the AST of the java file in JSON format.
	 */
	private static JSONObject parse(String filename, String sourceCode) {
		System.setErr(new PrintStream(new ByteArrayOutputStream()));
		JavaCompiler compiler = JavacTool.create();
		StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> units = null;
		if (sourceCode == null)
			units = manager.getJavaFileObjects(filename);
		else
			units = Arrays.asList(new JavaFileObject[] { new DynamicJavaSourceCodeObject(filename, sourceCode) });
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavacTask task = (JavacTask) compiler.getTask(null, manager, diagnostics, null, null, units);
		SignatureExtractor tscanner = new SignatureExtractor(units.iterator().next().getName());
		try {
			tscanner.scan(task.parse(), null);
			return tscanner.getJSON();
		} catch (IOException e) {
			return new JSONObject().append("Error", e.getMessage());
		}
	}

}
