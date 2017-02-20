ASTParser: Abstract Syntax Tree Parser for Java Source Code
===========================================================
ASTParser is an Abstract Syntax Tree (AST) Parser for Java source code, based
on the Java compiler. The tool allows exporting the AST of source code files
or projects in JSON format. The tool has a command line interface and can
also be used as a library.

Executing in Command Line mode
------------------------------
Execute as: <pre><code>java -jar ASTParser.jar -project="path/to/project"</code></pre>
for projects, or as: <pre><code>java -jar ASTParser.jar -file="path/to/file"</code></pre>
for java files.

Using as a library
------------------
Import the library in your code. Then, you can use it as follows:
- For folders containing java files:<pre><code>String ast = ASTParser.parseProject("path/to/folder/");</code></pre>
- For java files:<pre><code>String ast = ASTParser.parseFile("path/to/file.java");</code></pre>
- For contents of java files (i.e. strings):
<pre><code>String ast = ASTParser.parseString(""
			 + "import org.myclassimports;\n"
			 + "\n"
			 + "public class MyClass {\n"
			 + "	private int myvar;\n"
			 + "\n"
			 + "	public MyClass(int myvar) {\n"
			 + "		this.myvar = myvar;\n"
			 + "	}\n"
			 + "\n"
			 + "	public void getMyvar() {\n"
			 + "		return myvar;\n"
			 + "	}\n"
			 + "}\n"
			 );</code></pre>

Using in Python
---------------
ASTParser also has python bindings. Using the python wrapper is simple. At first, the library
has to be imported and the ASTParser object has to be initialized given the path to the jar
of the library:
<pre><code>ast_parser = ASTParser("path/to/ASTParser.jar")</code></pre>
After that, you can use it as follows:
- For folders containing java files:<pre><code>ast = ast_parser.parse_folder("path/to/folder/");</code></pre>
- For java files:<pre><code>ast = ast_parser.parse_file("path/to/file.java");</code></pre>
- For contents of java files (i.e. strings):
<pre><code>ast = ast_parser.parse_string(
			"import org.myclassimports;\n" + 
			"\n" + 
			"public class MyClass {\n" + 
			"   private int myvar;\n" + 
			"\n" + 
			"   public MyClass(int myvar) {\n" + 
			"      this.myvar = myvar;\n" + 
			"   }\n" + 
			"\n" + 
			"   public void getMyvar() {\n" + 
			"      return myvar;\n" + 
			"   }\n" + 
			"}\n"
	)</code></pre>
Note that after using the library, you have to close the ASTParser object using function <code>close</code>, i.e.:<pre><code>ast_parser.close()</code></pre>

