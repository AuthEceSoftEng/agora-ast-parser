package parser;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Class used to allow parsing string contents of java files.
 * 
 * @author themis
 */
public class DynamicJavaSourceCodeObject extends SimpleJavaFileObject {
	private String sourceCode;

	protected DynamicJavaSourceCodeObject(String name, String code) {
		super(URI.create("string:///" + name.replaceAll(".", "/") + Kind.SOURCE.extension), Kind.SOURCE);
		this.sourceCode = code;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return sourceCode;
	}
}
