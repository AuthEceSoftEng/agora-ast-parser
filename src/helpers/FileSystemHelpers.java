package helpers;

import java.io.File;
import java.util.ArrayList;

/**
 * Helper functions for handling file system operations.
 * 
 * @author themis
 */
public class FileSystemHelpers {

	/**
	 * Finds all the java files of a folder and all its subfolders recursively.
	 * 
	 * @param folderName the path to the folder.
	 * @param files a list that is filled with the java files of the folder and its subfolders.
	 */
	private static void findJavaFilesOfFolderRecursively(String folderName, ArrayList<File> files) {
		File root = new File(folderName);
		File[] list = root.listFiles();
		if (list == null)
			return;
		for (File file : list) {
			if (file.isDirectory())
				findJavaFilesOfFolderRecursively(file.getAbsolutePath(), files);
			else if (file.getName().endsWith(".java"))
				files.add(file.getAbsoluteFile());
		}
	}

	/**
	 * Finds all the java files of a folder and all its subfolders recursively.
	 * 
	 * @param folderName the path to the folder.
	 * @return a list containing the java files of the folder and its subfolders.
	 */
	public static ArrayList<File> getJavaFilesOfFolderRecursively(String folderName) {
		ArrayList<File> files = new ArrayList<File>();
		findJavaFilesOfFolderRecursively(folderName, files);
		return files;
	}

}
