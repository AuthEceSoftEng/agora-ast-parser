package parser;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;

/**
 * Handles the parsing of java files and the extraction of their Abstract Syntax Trees (ASTs).
 * 
 * @author themis
 */
public class SignatureExtractor extends TreeScanner<Boolean, Void> {

	/**
	 * The AST of the given java file.
	 */
	private JSONObject javafile;

	/**
	 * The filename of the given source code file.
	 */
	private String filename;

	/**
	 * Instantiates this tree scanner object.
	 * 
	 * @param filename the filename of the given source code file.
	 */
	public SignatureExtractor(String filename) {
		javafile = new JSONObject();
		javafile.put("otherclasses", new ArrayList<JSONObject>());
		this.filename = new File(filename).getName().split("\\.")[0];
	}

	/**
	 * Returns the AST of the parsed source code.
	 * 
	 * @return the AST of the parsed source code in JSON format.
	 */
	public JSONObject getJSON() {
		return javafile;
	}

	/**
	 * Parses the given class tree and returns its AST.
	 * 
	 * @param aclass the class tree that is parsed.
	 * @return the AST of the given class tree in JSON format.
	 */
	private JSONObject getClassJson(ClassTree aclass) {
		JSONObject theclass = new JSONObject();
		theclass.put("name", aclass.getSimpleName());
		if (aclass.getKind() == Tree.Kind.CLASS)
			theclass.put("type", "class");
		else if (aclass.getKind() == Tree.Kind.INTERFACE)
			theclass.put("type", "interface");
		theclass.put("extends", aclass.getExtendsClause());// aclass.getExtendsClause()
															// != null ?
															// aclass.getExtendsClause()
															// : "");
		ArrayList<String> theimplements = new ArrayList<String>();
		for (Tree tree : aclass.getImplementsClause()) {
			theimplements.add(tree.toString());
		}
		theclass.put("implements", theimplements);
		theclass.put("modifiers", aclass.getModifiers().getFlags());
		ArrayList<JSONObject> variables = new ArrayList<JSONObject>();
		ArrayList<JSONObject> methods = new ArrayList<JSONObject>();
		ArrayList<JSONObject> innerclasses = new ArrayList<JSONObject>();
		for (Tree tree : aclass.getMembers()) {
			if (tree.getKind() == Tree.Kind.VARIABLE) {
				VariableTree variable = (VariableTree) tree;
				JSONObject thevariable = new JSONObject();
				thevariable.put("name", variable.getName());
				thevariable.put("type", variable.getType());
				thevariable.put("modifiers", variable.getModifiers().getFlags());
				variables.add(thevariable);
			} else if (tree.getKind() == Tree.Kind.METHOD) {
				MethodTree method = (MethodTree) tree;
				JSONObject themethod = new JSONObject();
				themethod.put("name", method.getName());
				themethod.put("modifiers", method.getModifiers().getFlags());
				ArrayList<JSONObject> parameters = new ArrayList<JSONObject>();
				for (VariableTree variable : method.getParameters()) {
					JSONObject parameter = new JSONObject();
					parameter.put("name", variable.getName());
					parameter.put("type", variable.getType());
					parameters.add(parameter);
				}
				themethod.put("parameters", parameters);
				themethod.put("returntype",
						method.getReturnType() != null ? method.getReturnType() : aclass.getSimpleName());
				ArrayList<String> thethrows = new ArrayList<String>();
				for (ExpressionTree ttree : method.getThrows()) {
					thethrows.add(ttree.toString());
				}
				themethod.put("throws", thethrows);
				methods.add(themethod);
			} else if (tree.getKind() == Tree.Kind.CLASS || tree.getKind() == Tree.Kind.INTERFACE) {
				ClassTree innerclass = (ClassTree) tree;
				innerclasses.add(getClassJson(innerclass));
			}
		}
		theclass.put("variables", variables);
		theclass.put("methods", methods);
		theclass.put("innerclasses", innerclasses);
		return theclass;
	}

	@Override
	public Boolean visitClass(ClassTree node, Void p) {
		ClassTree aclass = node;
		if (aclass.getSimpleName().toString().equals(filename) || filename.equals("")) {
			javafile.put("class", getClassJson(aclass));
		} else {
			javafile.getJSONArray("otherclasses").put(getClassJson(aclass));
		}
		return false;
	}

	@Override
	public Boolean visitCompilationUnit(CompilationUnitTree node, Void p) {
		javafile.put("package", node.getPackageName() != null ? node.getPackageName() : "");
		ArrayList<String> theimports = new ArrayList<String>();
		for (ImportTree tree : node.getImports()) {
			theimports.add(tree.getQualifiedIdentifier().toString());
		}
		javafile.put("imports", theimports);
		return super.visitCompilationUnit(node, p);
	}

}
