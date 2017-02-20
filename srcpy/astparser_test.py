from astparser import ASTParser

if __name__ == '__main__':
	'''Used as a test for the python bindings'''
	ast_parser = ASTParser("../target/agora-ast-parser-0.1-0.4.jar")
	ast = ast_parser.parse_string(
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
	)
	print(ast)
	ast_parser.close()
