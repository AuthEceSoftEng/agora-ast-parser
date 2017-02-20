package astparser;

import java.nio.charset.Charset;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import parser.ASTParser;

/**
 * Class used to bind this library to a python file.
 * 
 * @author themis
 */
public class PythonBinder {

	/**
	 * Function used to bind this library to a python file. It works by opening a {@link Scanner} on the standard input,
	 * reading the required task in the form of a message and writing the result as a message in the standard output.
	 * The base 64 format is used to send the messages.
	 * 
	 * @param args the one and only parameter must be the path to the properties file.
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			// Receive message and decode it
			String b64message = scanner.nextLine();
			String message = new String(DatatypeConverter.parseBase64Binary(b64message));

			// Operate on message and return response
			String messageresult = "";
			if (message.equals("START_OF_TRANSMISSION")) {
				messageresult = message;
				String b64messageresult = DatatypeConverter
						.printBase64Binary(messageresult.getBytes(Charset.forName("US-ASCII")));
				System.out.println(b64messageresult);
				System.out.flush();
				continue;
			} else if (message.equals("END_OF_TRANSMISSION")) {
				messageresult = message;
				String b64messageresult = DatatypeConverter
						.printBase64Binary(messageresult.getBytes(Charset.forName("US-ASCII")));
				System.out.println(b64messageresult);
				System.out.flush();
				break;
			} else {
				String messageHeader = message.split("_-_")[0];
				message = message.substring(messageHeader.length() + 3);
				String parseType = messageHeader.split("_")[1];
				if (parseType.equals("STRING"))
					messageresult = ASTParser.parseString(message);
				else if (parseType.equals("FILE"))
					messageresult = ASTParser.parseFile(message);
				else if (parseType.equals("FOLDER"))
					messageresult = ASTParser.parseProject(message);
				String b64messageresult = DatatypeConverter
						.printBase64Binary(messageresult.getBytes(Charset.forName("US-ASCII")));
				System.out.println(b64messageresult);
				System.out.flush();
			}
		}
		scanner.close();
	}

}
